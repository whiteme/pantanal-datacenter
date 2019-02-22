package com.pantanal.data.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataImportService {

    private static Logger logger = LoggerFactory.getLogger(DataImportService.class);


    @Autowired
    private JdbcTemplate xuwuTemplate;



    private void importRentInfoData(File file){
        ObjectMapper om = new ObjectMapper();
        try {
            List data = om.readValue(file , ArrayList.class);
            System.out.println(1);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String[] getFields(String db, String table) {
        return xuwuTemplate.queryForList("SELECT COLUMN_NAME FROM information_schema.COLUMNS WHERE table_name = ? AND table_schema = ?"
                , new Object[]{table, db}, String.class)
                .toArray(new String[]{});
    }


    public static void main(String args[]){
        DataImportService service = new DataImportService();
        File f = new File("/Users/shenn-litscope/git-Litscope/data-cloud/lianjia-bj-20190122.json");
        service.importRentInfoData(f);
    }

}
