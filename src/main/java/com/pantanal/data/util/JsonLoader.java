package com.pantanal.data.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonLoader {

    private static Logger logger = Logger.getLogger(JsonLoader.class);

    public static Object[] loadJsonFile(String filename) {
        Object []ret = null;
        File jsonFile = new File(filename);
        try {
            String content = FileUtils.readFileToString(jsonFile);
            ObjectMapper om = new ObjectMapper();

            List data = om.readValue(jsonFile, ArrayList.class);
            ret = data.toArray();

        } catch (Exception e) {
            logger.error(" Load Json File Exeception " + filename);
            e.printStackTrace();
        }
        return ret;
    }


    public static void main(String args[]) {
        JsonLoader.loadJsonFile("/Users/shenn-litscope/git-Litscope/data-cloud/lianjia-bj-20190122.json");
    }

}
