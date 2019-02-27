package com.pantanal.data.task;

import com.pantanal.data.dao.SmartDAO;

import java.util.Map;
import java.util.Properties;

public class ConvertQuoteTask extends BaseTask {


    private SmartDAO smartDAO;

    private String source;

    private String beginDate;

    public ConvertQuoteTask(Map param, Properties conf , SmartDAO smartDAO) {
        super(param, conf);
        this.smartDAO = smartDAO;
        this.source = param.get("source").toString();
        this.beginDate = param.get("beginDate").toString();
    }

    @Override
    public void run() {

    }
}
