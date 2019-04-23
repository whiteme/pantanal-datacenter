package com.pantanal.data.web;

import com.pantanal.data.service.DataImportService;
import com.pantanal.data.service.house.RentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
@RestController
@RequestMapping(value = "/wx")
public class DataImportController {


    private static Logger logger = LoggerFactory.getLogger(DataImportController.class);


    @Autowired
    private DataImportService dataImportService;


    @Autowired
    private RentService rentService;


    @RequestMapping(value="/import/{import_type}" , method = RequestMethod.GET)
    public String entireImportRentInfo(@PathVariable() String import_type){
        logger.info("Receive Import task {} " , import_type);
        if("HOUSE".equals(import_type)){
            dataImportService.importTask(import_type);
        }else if("QUOTE".equals(import_type)){
            rentService.aggrPublicSourceRentInfo2Quote(false);
        }

        return "SUCCESS";
    }


    @GetMapping(value="/import/delta/{import_type}" )
    public String deltaImport(@PathVariable() String import_type ){
        logger.info("Receive Import task {} " , import_type);
        if("HOUSE".equals(import_type.toUpperCase())){
            dataImportService.importTask("DELTA");
        }else if("QUOTE".equals(import_type.toUpperCase())){
            rentService.aggrPublicSourceRentInfo2Quote(true);
        }

        return "SUCCESS";
    }
    public static void main(String args[]) throws UnsupportedEncodingException {
        System.out.println(URLEncoder.encode("https://www.baidu.com" , "utf-8"));
    }


}
