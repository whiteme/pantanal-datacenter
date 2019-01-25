package com.pantanal.data.dao;

import com.pantanal.data.entity.Community;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class QuoteDao {

    @Autowired
    @Qualifier("xuwuJdbcTemplate")
    private JdbcTemplate xuwuJdbcTemplate;


    public List<Map> selectWeekSeriesQuote(){
        String sql = "select distinct(DATE_FORMAT(STR_TO_DATE(date,'%Y%m%d%H%i%S'),'%Y-%m-%U')) as xaxis , " +
                "avg(avg_sm) as avg_price , avg(volume) as volume from quote group by xaxis;";
        return xuwuJdbcTemplate.query(sql , new RowMapper<Map>(){
            @Override
            public Map mapRow(ResultSet rs, int rowNum) throws SQLException {
                Map ret = new HashMap();
                ret.put("x" , rs.getString("xaxis").toString());
                ret.put("price" , Double.parseDouble(rs.getString("avg_price")));
                ret.put("volume" , Double.parseDouble(rs.getString("volume")));
                return ret;
            }
        });

    }


}
