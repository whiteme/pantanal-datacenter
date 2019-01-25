package com.pantanal.data.web;

import com.alibaba.fastjson.JSONObject;
import com.pantanal.data.quote.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/wx")
public class QuoteController {

    @Autowired
    private QuoteService quoteService;


    @RequestMapping(value="/quote/renthouse" , method = RequestMethod.GET)
    public String getWeeklyRentHouseQuoteSeries(){
        String retString = null;
        Map tmp = new HashMap();
        List xaxis = new ArrayList<String>();
        List price = new ArrayList<Double>();
        List vol = new ArrayList<Double>();
        tmp.put("x" , xaxis);
        tmp.put("price" , price);
        tmp.put("vol", vol);

        List<Map> data = quoteService.getQuoteData();

        for(Map m : data){
            xaxis.add(m.get("x").toString());
            price.add(m.get("price"));
            vol.add(m.get("volume"));
        }

        retString = JSONObject.toJSONString(tmp);
        return retString;
    }
}
