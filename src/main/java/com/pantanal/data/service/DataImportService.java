package com.pantanal.data.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pantanal.data.task.Import2DBTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class DataImportService {

    private static Logger logger = LoggerFactory.getLogger(DataImportService.class);


    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 8, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());


    private final static String DEFAULT_LAST_IMPORT_DATE = "20000101";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${raw.file.dir}")
    private String RAW_FILE_DIR;


    @Value("${import.db.name}")
    private String IMPORT_DB_NAME;

    @Value("${import.table.name}")
    private String IMPORT_TABLE_NAME;

    @Value("${import.tmp.dir}")
    private String IMPORT_TMP_DIR;

    public static final String TASK_PARAM_SOURCE_PATH  = "TASK_PARAM_SOURCE_PATH";

    public static final String TASK_PARAM_TMP_FILE_PATH = "TASK_PARAM_TMP_FILE_PATH";

    public static final String TASK_PARAM_TABLENAME = "TASK_PARAM_TABLENAME";

    public static final String TASK_PARAM_DBNAME = "TASK_PARAM_DBNAME";

    public static final String TASK_PARAM_SOURCE = "TASK_PARAM_SOURCE";

    public static final String TASK_PARAM_CREATEDATE = "TASK_PARAM_CREATEDATE";


    public String[] getFields(String db, String table) {
        return jdbcTemplate.queryForList("SELECT COLUMN_NAME FROM information_schema.COLUMNS WHERE table_name = ? AND table_schema = ?"
                , new Object[]{table, db}, String.class)
                .toArray(new String[]{});
    }


    @Scheduled(cron = "0 0 * * * ?")
    public void doDetlaImport(){
        logger.info("start import detla");
        importTask("DELTA");
    }


    public void importTask(String importMethod){
        String beginDate = DEFAULT_LAST_IMPORT_DATE;
        if("DELTA".equals(importMethod)){
            beginDate = getLastImportDate();
        }

        File raw_dir = new File(RAW_FILE_DIR);
        String finalBeginDate = beginDate;
        File[] rawFiles = raw_dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                boolean ret = false;
                if(name.endsWith("json")){
                    name = name.substring(0 , name.length() - 6);
                    String [] nameParts = name.split("-");
                    ret = nameParts[nameParts.length-1].compareTo(finalBeginDate) > 0;
                }
                return ret;
            }
        });
        String fields[]  = getFields(IMPORT_DB_NAME , IMPORT_TABLE_NAME);
        for(File file : rawFiles){

            String fileName = file.getName();
            String [] nameParts = fileName.split("-");
            String tmpFileName = UUID.randomUUID().toString();


            Map param = new HashMap();
            param.put(TASK_PARAM_TMP_FILE_PATH, String.format(IMPORT_TMP_DIR , tmpFileName ));
            param.put(TASK_PARAM_SOURCE_PATH , file.getAbsolutePath());
            param.put(TASK_PARAM_TABLENAME,IMPORT_TABLE_NAME);
            param.put(TASK_PARAM_DBNAME,IMPORT_DB_NAME);
            param.put(TASK_PARAM_SOURCE,nameParts[0]);
            param.put(TASK_PARAM_CREATEDATE,nameParts[nameParts.length-1]);




            Import2DBTask task  = new Import2DBTask(param , null , null , null ,jdbcTemplate , fields);
            threadPoolExecutor.execute(task);


        }


    }


    @PostConstruct
    public void init(){

    }


    public String getLastImportDate(){
        String ret = DEFAULT_LAST_IMPORT_DATE;
        String sql = "select max( DATE_FORMAT(filetime , '%Y%m%d')) as lastdate from importlog;";
        List<String> last = jdbcTemplate.query(sql , new RowMapper<String>(){

            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("lastdate");
            }
        });
        if(last != null && ! last.isEmpty()) ret = last.get(0);
        return ret;
    }


    public static void main(String args[]){
        File raw_dir = new File("/Users/shenn-litscope/git-Litscope/data-cloud");
        File[] rawFile = raw_dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {

                boolean ret = false;
                if(name.endsWith("json")){
                    name = name.substring(0 , name.length() - 6);
                    System.out.println(name);
                    String [] nameParts = name.split("-");
                    ret = nameParts[nameParts.length-1].compareTo("20190121") > 0;
                }

                return ret;
            }
        });
    }



}
