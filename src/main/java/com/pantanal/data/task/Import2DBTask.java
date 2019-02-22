package com.pantanal.data.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pantanal.data.entity.House;
import com.pantanal.data.entity.ImportLog;
import com.pantanal.data.func.RawDataTransFunc;
import com.pantanal.data.service.DataImportService;
import com.pantanal.data.util.DateUtil;
import com.pantanal.data.util.StringUtil;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.lang.math.NumberUtils;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Import2DBTask extends BaseTask {

    private static Logger logger = LoggerFactory.getLogger(Import2DBTask.class);

    private final static String GLOBAL_DEFAULT_VALUE = "\\N";

    private final static String FIELD_SPLIT = "\t";

//    private static SimpleDateFormat sdf = "yyyyMMdd";

    private String tableName;

    private String dbName;

    private String sourceFilePath;

    private String[] fields;

    private String tempFilePath;

    private Map<String, List<RawDataTransFunc>> rawDataTransConf;

    private Map<String, String> defaultValue;

    private JdbcTemplate jdbcTemplate;




    public Import2DBTask(Map param, Properties conf,
                         Map<String, List<RawDataTransFunc>> valueFunc,
                         Map<String, String> defaultValue,
                         JdbcTemplate jdbcTemplate,
                         String[] fields) {
        super(param, conf);
        this.jdbcTemplate = jdbcTemplate;
        this.tempFilePath = param.get(DataImportService.TASK_PARAM_TMP_FILE_PATH).toString();
        this.sourceFilePath = param.get(DataImportService.TASK_PARAM_SOURCE_PATH).toString();
        this.rawDataTransConf = valueFunc;
        this.defaultValue = defaultValue;
        this.fields = fields;
        this.tableName = param.get(DataImportService.TASK_PARAM_TABLENAME).toString();
        this.dbName = param.get(DataImportService.TASK_PARAM_DBNAME).toString();
    }

    @Override
    public void run() {
        File sourceFile = new File(sourceFilePath);
        Assert.assertFalse(!sourceFile.exists());
        ObjectMapper om = new ObjectMapper();
        try {
            List data = om.readValue(sourceFile, ArrayList.class);
            generateCSVTempFile(data);
            loadCSVFile2DB();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

    }

    private void loadCSVFile2DB() {

        long startTime = System.currentTimeMillis();
        String sqlTemplate = "LOAD DATA LOCAL INFILE '%s' INTO TABLE %s;";
        String sql = String.format(sqlTemplate, this.tempFilePath, this.tableName);
        int i = jdbcTemplate.update(sql); //tidb 不支持IGNORE
        logger.info(sql + "写入数据:" + i + "\t时间:" + (System.currentTimeMillis() - startTime));

        File file = new File(this.tempFilePath);
        file.delete();

        insertInportLog(i);
    }

    private void insertInportLog(int total){
        ImportLog importLog = new ImportLog();
        importLog.setFileName(getParam().get(DataImportService.TASK_PARAM_SOURCE_PATH).toString());
        importLog.setSource("");
        importLog.setDay(0);
        importLog.setCreateDate(new Date());

        Date filetime = DateUtil.toDate(getParam().get(DataImportService.TASK_PARAM_CREATEDATE).toString() , "yyyyMMdd");
        importLog.setFileTime(filetime);
        importLog.setTotal(total);
        save(importLog);
    }

    public boolean save(ImportLog importLog) {
        String SQL = "insert into `importlog` " +
                "(source,city,day,filename,filetime,total,createdate)";
        SQL += " values(?,?,?,?,?,?,?)";
        Object args[] = { importLog.getSource(), importLog.getCity(), importLog.getDay(), importLog.getFileName(), importLog.getFileTime(),
                importLog.getTotal(), importLog.getCreateDate() };
        int temp = jdbcTemplate.update(SQL, args);
        if (temp > 0) {
            return true;
        } else {
            return false;
        }
    }


    private Map lowkeyMap(Map map) {
        Map ret = new HashMap();
        for (Object keyObj : map.keySet()) {
            String key = (String) keyObj;
            ret.put(key.toLowerCase(), map.get(keyObj));
        }
        return ret;
    }


    private void generateCSVTempFile(List data) throws IOException {
        FileWriterWithEncoding fw = null;
        try {
            fw = new FileWriterWithEncoding(this.tempFilePath, "UTF-8");
            fw.write(generateContent(data));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fw.close();
        }

    }


    private String generateContent(List data) {
        StringBuffer sb = new StringBuffer();
        for (Object item : data) {
            Map rawMap = (Map) item;
            rawMap = lowkeyMap(rawMap);
            for (String fieldName : this.fields) {
                String outVal = GLOBAL_DEFAULT_VALUE;
                if (rawMap.containsKey(fieldName)) {
                    Object raw = rawMap.get(fieldName);
                    ArrayList valList = (ArrayList) raw;
                    if (!valList.isEmpty())
                        outVal = processRawValue(fieldName, valList);
                }

                if (outVal.equals(GLOBAL_DEFAULT_VALUE)) {
                    //补充通过原始数据扩展出的内容
                    outVal = fixExtField4Raw(fieldName, rawMap);

                }
                sb.append(outVal + FIELD_SPLIT);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private String fixExtField4Raw(String fieldName, Map rawMap) {
        String retString = GLOBAL_DEFAULT_VALUE;
        if ("city".equals(fieldName)) {
            retString = "bj";
        }
        if ("id".equals(fieldName)) {
            retString = UUID.randomUUID().toString();
        }

        if ("bid".equals(fieldName)) {
            retString = UUID.randomUUID().toString();
            //xxx:todo md5 "XUWU" + source + city + comunity + price + square + expore_date

        }

        if (fieldName.equals("elevator") && rawMap.containsKey("elevator") && !((ArrayList) rawMap.get("elevator")).isEmpty()) {
            String elevator = ((ArrayList) rawMap.get("elevator")).get(0).toString();
            if ("有".equals(elevator)) {
                retString = "1";
            } else if ("无".equals(elevator)) {
                retString = "0";
            }
        }


        if (rawMap.containsKey("subway") && !((ArrayList) rawMap.get("subway")).isEmpty()) {
            String rawSubway = ((ArrayList) rawMap.get("subway")).get(0).toString();
            String[] tmps = rawSubway.split("\\s+\\-\\s+");
            if ("subway".equals(fieldName) && tmps.length > 0) {
                retString = tmps[0];
            }
            if ("subwaystation".equals(fieldName) && tmps.length > 1) {
                retString = tmps[1];
            }
        }

        if (rawMap.containsKey("layout") && !((ArrayList) rawMap.get("layout")).isEmpty()) {
            String rawLayout = ((ArrayList) rawMap.get("layout")).get(0).toString();
            Double[] ds = StringUtil.getDoubleArray(rawLayout);


            if ("room".equals(fieldName) && ds.length > 0) {
                retString = ds[0].intValue() + "";
            }
            if ("hall".equals(fieldName) && ds.length > 1) {
                retString = ds[1].intValue() + "";
            }
            if ("toilet".equals(fieldName) && ds.length > 2) {
                retString = ds[2].intValue() + "";
            }
        }

        if (rawMap.containsKey("floor") && !((ArrayList) rawMap.get("floor")).isEmpty()) {
            String floor = ((ArrayList) rawMap.get("floor")).get(0).toString();
            String[] tmps = floor.split("/");
            if ("floorlevel".equals(fieldName) && tmps.length > 0) {
                if ("高楼层".equals(tmps[0])) {
                    retString = House.FLOORLEVEL_HIGH + "";
                } else if ("中楼层".equals(tmps[0])) {
                    retString = House.FLOORLEVEL_MIDDLE + "";
                } else if ("低楼层".equals(tmps[0])) {
                    retString = House.FLOORLEVEL_LOW + "";
                } else {
                    if (StringUtil.getInt(tmps[0]) == null) {
                        retString = House.FLOORLEVEL_UNKNOWN + "";
                    } else {
                        retString = StringUtil.getInt(tmps[0]) + "";
                    }
                }
            }
            if ("maxfloor".equals(fieldName) && tmps.length > 1) {
                retString = StringUtil.getInt(tmps[1]) + "";
            }
        }
        //fill by task conf param map
        if (fieldName.equals("source")) {
            retString = super.getParam().get(DataImportService.TASK_PARAM_SOURCE).toString();
        }

        if (fieldName.equals("createdate")) {
            retString = super.getParam().get(DataImportService.TASK_PARAM_CREATEDATE).toString();
        }
        return retString;

    }


    private String processRawValue(String fieldName, ArrayList valList) {
        String retString = valList.get(0).toString();


        if (fieldName.equals("renttype")) {
            if ("整租".equals(retString)) {
                retString = House.RENTTYPE_ALL + "";
            } else {
                retString = House.RENTTYPE_JOIN + "";
            }
        }

        if (fieldName.equals("square")) {
            retString = StringUtil.getDouble(retString) + "";
        }


        if (fieldName.equals("orientation")) {
            retString = retString.replace("朝", "-").replaceAll("\\s+", "-");
        }


        if (fieldName.equals("latitude")) {
            retString = StringUtil.getDouble(retString) + "";
        }

        if (fieldName.equals("longtitude")) {
            retString = StringUtil.getDouble(retString) + "";
        }

        if (fieldName.equals("elevator")) {
            if ("有".equals(retString)) {
                retString = "1";
            } else if ("无".equals(retString)) {
                retString = "0";
            }
        }
        return retString;
    }


    public static void main(String args[]) {
//        String[] fields = new String[]{"price", "renttype", "exposuredate", "latitude", "landmark", "city"};
//        Map param = new HashMap();
//        param.put("tempFilePath", "/Users/shenn-litscope/git-Litscope/data-cloud/test-csv.csv");
//        param.put("sourceFilePath", "/Users/shenn-litscope/git-Litscope/data-cloud/lianjia-bj-20190122.json");
//        param.put("source","lianjia");
//        param.put("createdate","20190221");
//        Import2DBTask t = new Import2DBTask(param, null, null, null, fields);
//        t.run();
    }
}
