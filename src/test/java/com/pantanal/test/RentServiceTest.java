package com.pantanal.test;

import com.pantanal.data.PantanalApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.alibaba.fastjson.JSONObject;
import com.pantanal.data.house.RentService;
import com.pantanal.data.quote.QuoteService;
import com.pantanal.data.service.ProxyIpService;
import com.pantanal.data.task.TaskManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PantanalApplication.class)
public class RentServiceTest {

  @Autowired
  private RentService service;

  @Autowired
  public QuoteService quoteService;
  @Autowired
  private TaskManager taskManager;

  @Autowired
  private ProxyIpService proxyIpService;
  @Test
  public void contextLoads() {
  }
  
  @Test
  public void testCombine() {
    // service.handleRentHouseDaily("","北京","","");

    System.out.println(JSONObject.toJSONString(quoteService.getQuoteData()));
  }

  @Test
  public void testImportHouse() {

    taskManager.importHouse();
  }

  
  @Test
  public void testProxyIp() {
    proxyIpService.checkProxyIp();
  }
}
