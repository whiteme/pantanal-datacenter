package com.pantanal.data.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.pantanal.data.entity.House;

import java.util.ArrayList;
import java.util.List;

@Repository
public class HouseDao {

    @Autowired
    @Qualifier("xuwuJdbcTemplate")
    private JdbcTemplate xuwuJdbcTemplate;


    private static String insertSql = "insert into `house` (city,name,communityuid,distirct,landmark,room,hall,toilet,price,renttype,size,orien,subway,subwaystation,latitude,longtitude,exposuredate,maxfloor,floorlevel,elevator,visitcnt,createdate,source)"
            + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    private static Object[] getInsertParams(House house) {
        return new Object[]{house.getCity(), house.getName(), house.getCommunityUid(), house.getDistirct(), house.getLandmark(), house.getRoom(),
                house.getHall(), house.getToilet(), house.getPrice(), house.getRentType(), house.getSize(), house.getOrien(), house.getSubway(),
                house.getSubwayStation(), house.getLatitude(), house.getLongtitude(), house.getExposureDate(), house.getMaxFloor(), house.getFloorLevel(),
                house.getElevator(), house.getVisitCnt(), house.getCreateDate(), house.getSource()};
    }

    public boolean save(House house) {
        int temp = xuwuJdbcTemplate.update(insertSql, getInsertParams(house));
        if (temp > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean save(List<House> houseList) {
        List<Object[]> paramList = new ArrayList<Object[]>();
        for (House house : houseList) {
            paramList.add(getInsertParams(house));
        }
        int[] temp = xuwuJdbcTemplate.batchUpdate(insertSql, paramList);

        return true;

    }

}
