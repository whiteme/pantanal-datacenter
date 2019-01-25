package com.pantanal.data.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeoUtil {

    public static final String LONG_REG = "\\d+\\.\\d+";

    public static final String LAT_REG = "^[\\-\\+]?([0-8]?\\d{1}\\.\\d{1,5}|90\\.0{1,5})$";



    public static String getLongtitude(String source){
        String ret = null;
        Pattern LONG_PART = Pattern.compile(LONG_REG);
        Matcher m = LONG_PART.matcher(source);
        m.find();
        ret = m.group();
        return ret;
    }

    public static void main(String args[]){
        System.out.println("===== + = : " + getLongtitude("116.50778"));
    }
}
