package com.pantanal.data.task;

import java.util.Map;
import java.util.Properties;

public abstract class BaseTask implements Runnable {

    private Map param ;

    private Properties conf;

    public BaseTask(Map param , Properties conf){
        this.param = param;
        this.conf = conf;
    }

    public Map getParam() {
        return param;
    }

    public void setParam(Map param) {
        this.param = param;
    }

    public Properties getConf() {
        return conf;
    }

    public void setConf(Properties conf) {
        this.conf = conf;
    }
}
