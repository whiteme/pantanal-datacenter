package com.pantanal.data.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.pantanal.data.entity.House;

@Repository
public class HouseDao {

  @Autowired
  @Qualifier("xuwuJdbcTemplate")
  private JdbcTemplate xuwuJdbcTemplate;

  public boolean save(House house) {
    String sql = "insert into `house` (city,name,communityuid,distirct,landmark,room,hall,toilet,price,renttype,size,orien,subway,subwaystation,latitude,longtitude,exposuredate,maxfloor,floorlevel,elevator,visitcnt,createdate,source)";
    sql += " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    Object args[] = { house.getCity(), house.getName(), house.getCommunityUid(), house.getDistirct(), house.getLandmark(), house.getRoom(),
        house.getHall(), house.getToilet(), house.getPrice(), house.getRentType(), house.getSize(), house.getOrien(), house.getSubway(),
        house.getSubwayStation(), house.getLatitude(), house.getLongtitude(), house.getExposureDate(), house.getMaxFloor(), house.getFloorLevel(),
        house.getElevator(), house.getVisitCnt(), house.getCreateDate(), house.getSource() };
    int temp = xuwuJdbcTemplate.update(sql, args);
    if (temp > 0) {
      return true;
    } else {
      return false;
    }
  }
}
