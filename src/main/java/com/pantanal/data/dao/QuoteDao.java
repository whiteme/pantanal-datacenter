package com.pantanal.data.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
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
    private JdbcTemplate jdbcTemplate;


    public List<Map> selectWeekSeriesQuote(){
        String sql = "select distinct(DATE_FORMAT(STR_TO_DATE(date,'%Y%m%d%H%i%S'),'%Y-%m-%U')) as xaxis , " +
                "avg(avg_sm) as avg_price , avg(volume) as volume from quote group by xaxis;";
        return jdbcTemplate.query(sql , new RowMapper<Map>(){
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


    public List<Map<String,String>> selectLatestQuoteInfo(int type) {
        String sql = "select source , max(date) as latest_date from quote where type=? group by source";
        return jdbcTemplate.query(sql , new Object[]{type} , new RowMapper<Map<String , String>>(){

            @Nullable
            @Override
            public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
                Map<String , String> ret = new HashMap<String , String>();
                ret.put("source" , rs.getString("source").toString());
                ret.put("laest_date" , rs.getString("latest_date").toString());
                return ret;
            }
        });
    }

    public List<String> selectHouseDataSources() {
        String sql = "select distinct(source) from house_raw";
        return jdbcTemplate.query(sql , new RowMapper<String>(){

            @Nullable
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString(1).toString();
            }
        });
    }
}
