package com.pantanal.data.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pantanal.data.entity.House;
import com.pantanal.data.func.RawDataTransFunc;
import com.pantanal.data.util.StringUtil;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Import2DBTask extends BaseTask {

    private final static String GLOBAL_DEFAULT_VALUE = "\\N";

    private final static String FIELD_SPLIT = "\t";

    private String tableName;

    private String sourceFilePath;

    private String[] fields;

    private String tempFilePath;

    private Map<String, List<RawDataTransFunc>> rawDataTransConf;

    private Map<String, String> defaultValue;


    public Import2DBTask(Map param, Properties conf,
                         Map<String, List<RawDataTransFunc>> valueFunc, Map<String, String> defaultValue,
                         String[] fields) {
        super(param, conf);
        this.tempFilePath = param.get("tempFilePath").toString();
        this.sourceFilePath = param.get("sourceFilePath").toString();
        this.rawDataTransConf = valueFunc;
        this.defaultValue = defaultValue;
        this.fields = fields;
    }

    @Override
    public void run() {
        File sourceFile = new File(sourceFilePath);
        Assert.assertFalse(!sourceFile.exists());
        ObjectMapper om = new ObjectMapper();
        try {
            List data = om.readValue(sourceFile, ArrayList.class);
            generateCSVTempFile(data);
        } catch (IOException e) {
            e.printStackTrace();
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
            fw = new FileWriterWithEncoding(this.tempFilePath, "utf8");
            fw.write(generateContent(data));
            String content = "";
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

                if(outVal.equals(GLOBAL_DEFAULT_VALUE)){
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
            retString = super.getParam().get("source").toString();
        }

        if(fieldName.equals("createdate")){
            retString = super.getParam().get("createdate").toString();
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
        String[] fields = new String[]{"price", "renttype", "exposuredate", "latitude", "landmark", "city"};
        Map param = new HashMap();
        param.put("tempFilePath", "/Users/shenn-litscope/git-Litscope/data-cloud/test-csv.csv");
        param.put("sourceFilePath", "/Users/shenn-litscope/git-Litscope/data-cloud/lianjia-bj-20190122.json");
        param.put("source","lianjia");
        param.put("createdate","20190221");
        Import2DBTask t = new Import2DBTask(param, null, null, null, fields);
        t.run();
    }
}
