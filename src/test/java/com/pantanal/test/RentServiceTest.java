package com.pantanal.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.alibaba.fastjson.JSONObject;
import com.pantanal.data.PantanalApplication;
import com.pantanal.data.house.RentService;
import com.pantanal.data.quote.QuoteService;
import com.pantanal.data.task.TaskManager;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PantanalApplication.class)
@WebAppConfiguration
public class RentServiceTest {

  @Autowired
  private RentService service;

  @Autowired
  private QuoteService quoteService;
  @Autowired
  private TaskManager taskManager;

  @Test
  public void testCombine() {
    // service.handleRentHouseDaily("","北京","","");

    System.out.println(JSONObject.toJSONString(quoteService.getQuoteData()));
  }

  @Test
  public void testImportHouse() {
    // service.handleRentHouseDaily("","北京","","");
    taskManager.importHouse();
  }

}
