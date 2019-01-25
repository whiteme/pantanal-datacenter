package com.pantanal.data.entity;

public class RentQuoteEntity {

    private String code;

    private String entityName;

    private double high = 0d;

    private double low = Double.MAX_VALUE;

    private double highSquareMeter = 0d;

    private double lowSquareMeter = Double.MAX_VALUE;

    private double avgSquareMeter;

    private int totalSquareMeter = 0;

    private int volume;

    private String date;

    private int period;

    private String source;

    private int type;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getHighSquareMeter() {
        return highSquareMeter;
    }

    public void setHighSquareMeter(double highSquareMeter) {
        this.highSquareMeter = highSquareMeter;
    }

    public double getLowSquareMeter() {
        return lowSquareMeter;
    }

    public void setLowSquareMeter(double lowSquareMeter) {
        this.lowSquareMeter = lowSquareMeter;
    }

    public double getAvgSquareMeter() {
        return avgSquareMeter;
    }

    public void setAvgSquareMeter(double avgSquareMeter) {
        this.avgSquareMeter = avgSquareMeter;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTotalSquareMeter() {
        return totalSquareMeter;
    }

    public void setTotalSquareMeter(int totalSquareMeter) {
        this.totalSquareMeter = totalSquareMeter;
    }
}
