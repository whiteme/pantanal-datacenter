/**
 * 
 */
package com.pantanal.data.entity;

import java.util.Date;

/**
 * @author gudong
 *
 */
public class ImportLog {
  private Long id;
  private String source; // 来源
  private String city; // 城市
  private Integer day; // 数据时间,20181220
  private String fileName; // 文件名
  private java.util.Date fileTime; // 文件时间
  private Integer total; // 房源数
  private java.util.Date createDate; // 导入时间

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getIdColumn() {
    return "id";
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public Integer getDay() {
    return day;
  }

  public void setDay(Integer day) {
    this.day = day;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public java.util.Date getFileTime() {
    return fileTime;
  }

  public void setFileTime(java.util.Date fileTime) {
    this.fileTime = fileTime;
  }

  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }

  public java.util.Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(java.util.Date createDate) {
    this.createDate = createDate;
  }


  public static void main(String args[]){
    new Date("20180102");
  }

}
