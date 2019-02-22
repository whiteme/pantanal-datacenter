package com.pantanal.data.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonLoader {

  private static Logger logger = LoggerFactory.getLogger(JsonLoader.class);

  public static Object[] loadJsonFile(String filename) {
    Object[] ret = null;
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
