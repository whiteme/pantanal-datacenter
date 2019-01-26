/**
 * 
 */
package com.pantanal.data.entity;

/**
 * @author root
 *
 */
public class House {
  public static final int RENTTYPE_UNKNOWN = 0;
  public static final int RENTTYPE_JOIN = 1;
  public static final int RENTTYPE_ALL = 2;

  public static final int FLOORLEVEL_UNKNOWN = 0;
  public static final int FLOORLEVEL_LOW = -1;
  public static final int FLOORLEVEL_MIDDLE = -2;
  public static final int FLOORLEVEL_HIGH = -3;

  private Long id;
  private String city; // 城市
  private String name; // 名字
  private String communityUid; // 小区uid
  private String distirct; // 区县
  private String landmark; // 商圈
  private Integer room; // 几室
  private Integer hall; // 几厅
  private Integer toilet; // 卫
  private Integer price; // 价格元
  private Integer rentType; // 0:UNKNOWN;1:JOIN;2:ALL
  private Integer size; // 面积，平方
  private String orien; // 格式:-朝向,如:-东南-南-北
  private String subway; // 地铁线路:9号线,14号线(西段)
  private String subwayStation; // 地铁站名
  private Double latitude; // 纬度
  private Double longtitude; // 经度
  private java.util.Date exposureDate; // 上架时间
  private Integer maxFloor; // 最高楼层
  private Integer floorLevel; // 0:UNKNOWN;-1:LOW;-2:MIDDLE;-3:HIGH
  private Boolean elevator; // 是否有电梯
  private Integer visitCnt; // 看房次数
  private java.util.Date createDate; // 创建时间
  private String source; // 来源

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCommunityUid() {
    return communityUid;
  }

  public void setCommunityUid(String communityUid) {
    this.communityUid = communityUid;
  }

  public String getDistirct() {
    return distirct;
  }

  public void setDistirct(String distirct) {
    this.distirct = distirct;
  }

  public String getLandmark() {
    return landmark;
  }

  public void setLandmark(String landmark) {
    this.landmark = landmark;
  }

  public Integer getRoom() {
    return room;
  }

  public void setRoom(Integer room) {
    this.room = room;
  }

  public Integer getHall() {
    return hall;
  }

  public void setHall(Integer hall) {
    this.hall = hall;
  }

  public Integer getToilet() {
    return toilet;
  }

  public void setToilet(Integer toilet) {
    this.toilet = toilet;
  }

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public Integer getRentType() {
    return rentType;
  }

  public void setRentType(Integer rentType) {
    this.rentType = rentType;
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public String getOrien() {
    return orien;
  }

  public void setOrien(String orien) {
    this.orien = orien;
  }

  public String getSubway() {
    return subway;
  }

  public void setSubway(String subway) {
    this.subway = subway;
  }

  public String getSubwayStation() {
    return subwayStation;
  }

  public void setSubwayStation(String subwayStation) {
    this.subwayStation = subwayStation;
  }

  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public Double getLongtitude() {
    return longtitude;
  }

  public void setLongtitude(Double longtitude) {
    this.longtitude = longtitude;
  }

  public java.util.Date getExposureDate() {
    return exposureDate;
  }

  public void setExposureDate(java.util.Date exposureDate) {
    this.exposureDate = exposureDate;
  }

  public Integer getMaxFloor() {
    return maxFloor;
  }

  public void setMaxFloor(Integer maxFloor) {
    this.maxFloor = maxFloor;
  }

  public Integer getFloorLevel() {
    return floorLevel;
  }

  public void setFloorLevel(Integer floorLevel) {
    this.floorLevel = floorLevel;
  }

  public Boolean getElevator() {
    return elevator;
  }

  public void setElevator(Boolean elevator) {
    this.elevator = elevator;
  }

  public Integer getVisitCnt() {
    return visitCnt;
  }

  public void setVisitCnt(Integer visitCnt) {
    this.visitCnt = visitCnt;
  }

  public java.util.Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(java.util.Date createDate) {
    this.createDate = createDate;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

}
