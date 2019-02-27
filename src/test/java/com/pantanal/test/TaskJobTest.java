package com.pantanal.test;


import com.pantanal.data.PantanalApplication;
import com.pantanal.data.service.DataImportService;
import com.pantanal.data.task.Import2DBTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PantanalApplication.class)
public class TaskJobTest {

    private static Logger logger = LoggerFactory.getLogger(TaskJobTest.class);


    @Autowired
    private DataImportService service;


    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Test
    public void testGenerateFile(){
        String fields[]  = service.getFields("xuwu" , "house_raw");

        logger.info("{}" , fields);
        Map param = new HashMap();
        param.put("tempFilePath", "/Users/shenn-litscope/git-Litscope/data-cloud/test-csv.csv");
        param.put("sourceFilePath" , "/Users/shenn-litscope/git-Litscope/data-cloud/lianjia-bj-20190122.json");
        param.put("source","lianjia");
        param.put("tableName","house_raw");
        param.put("dbName","xuwu");
        param.put("source","lianjia");
        param.put("createdate","20190221");
        Import2DBTask t = new Import2DBTask(param ,
                    null ,
                    null ,
                    null ,
                    jdbcTemplate,
                    fields);
        t.run();
    }

    @Test
    public void testLastFiletime(){
        System.out.println(service.getLastImportDate());
//        assert(service.getLastImportDate().equals("20190216"));
    }
}
