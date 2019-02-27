package com.pantanal.test;

import com.pantanal.data.PantanalApplication;
import com.pantanal.data.dao.HouseDao;
import com.pantanal.data.dao.SmartDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.alibaba.fastjson.JSONObject;
import com.pantanal.data.service.house.RentService;
import com.pantanal.data.service.quote.QuoteService;
import com.pantanal.data.service.ProxyIpService;
import com.pantanal.data.task.TaskManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PantanalApplication.class)
public class RentServiceTest {


  static Logger logger = LoggerFactory.getLogger(RentServiceTest.class);

  @Autowired
  private RentService service;

  @Autowired
  public QuoteService quoteService;
  @Autowired
  private TaskManager taskManager;

  @Autowired
  private ProxyIpService proxyIpService;

  @Autowired
  private RentService rentService;


  @Autowired
  private HouseDao houseDao;

  @Autowired
  private SmartDAO smartDAO;

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


  @Test
  public void testAggrRentHouseInfo(){
        rentService.aggrPublicSourceRentInfo2Quote();
  }


  @Test
  public void testSmartDAO(){
      try {
          logger.info("count is {} " , smartDAO.getCount("select count(1) from house_raw where source = ? and createdate > ?" , new Object[]{"lianjia" , "2001-01-01"}));
      } catch (Exception e) {
          e.printStackTrace();
      }

//      logger.info("data {}" , houseDao.selectHouseRaw("select * from house_raw where source=? and createdate > 2019-02-01 limit 0 , 1000", new Object[]{"lianjia"}));
  }

}
