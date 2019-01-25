package com.pantanal.data.dao;

import com.pantanal.data.entity.Community;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CommunityDao {

    @Autowired
    @Qualifier("xuwuJdbcTemplate")
    private JdbcTemplate xuwuJdbcTemplate;

    public List<Community> select(){
        String sql = "select * from community";
        return xuwuJdbcTemplate.query(sql ,
                new RowMapper<Community>(){
                    @Override
                    public Community mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Community c = new Community();
                        if(rs != null){
                            String name = rs.getString("name");
                            c.setCommunityName(name);

                            String code = rs.getString("uid");
                            c.setCode(code);

                        }
                        return c;
                    }
                });
    }
}
