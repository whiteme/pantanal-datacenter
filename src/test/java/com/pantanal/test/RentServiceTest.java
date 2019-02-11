package com.pantanal.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONObject;
import com.pantanal.data.house.RentService;
import com.pantanal.data.quote.QuoteService;
import com.pantanal.data.service.ProxyIpService;
import com.pantanal.data.task.TaskManager;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RentServiceTest {

  @Autowired
  private RentService service;

  @Autowired
  private QuoteService quoteService;
  @Autowired
  private TaskManager taskManager;

  @Autowired
  private ProxyIpService proxyIpService;
  
  
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

  
  @Test
  public void testProxyIp() {
    proxyIpService.checkProxyIp();
  }
}
