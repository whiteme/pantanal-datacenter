package com.pantanal.test;

import com.alibaba.fastjson.JSONObject;
import com.pantanal.data.PantanalApplication;
import com.pantanal.data.dao.QuoteDao;
import com.pantanal.data.house.RentService;
import com.pantanal.data.quote.QuoteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PantanalApplication.class)
@WebAppConfiguration
public class RentServiceTest {

    @Autowired
    private RentService service;

    @Autowired
    private QuoteService quoteService;

    @Test
    public void testCombine(){
//        service.handleRentHouseDaily("","北京","","");

        System.out.println(JSONObject.toJSONString(quoteService.getQuoteData()));
    }

}
