package com.pantanal.data.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.pantanal.data.entity.ImportLog;

@Repository
public class ImportLogDao {

  @Autowired
  @Qualifier("xuwuJdbcTemplate")
  private JdbcTemplate xuwuJdbcTemplate;

  public boolean save(ImportLog importLog) {
    String sql = "insert into `importlog` (source,city,day,filename,filetime,total,createdate)";
    sql += " values(?,?,?,?,?,?,?)";
    Object args[] = { importLog.getSource(), importLog.getCity(), importLog.getDay(), importLog.getFileName(), importLog.getFileTime(),
        importLog.getTotal(), importLog.getCreateDate() };
    int temp = xuwuJdbcTemplate.update(sql, args);
    if (temp > 0) {
      return true;
    } else {
      return false;
    }
  }

  public ImportLog getByFileName(String fileName) {
    String sql = "select id,source,city,day,filename,filetime,total,createdate from `importlog` where filename=?";

    List<ImportLog> list = xuwuJdbcTemplate.query(sql, new RowMapper<ImportLog>() {
      @Override
      public ImportLog mapRow(ResultSet rs, int rowNum) throws SQLException {
        ImportLog ret = new ImportLog();
        ret.setId(rs.getLong("id"));
        ret.setSource(rs.getString("source"));
        ret.setCity(rs.getString("city"));
        ret.setDay(rs.getInt("day"));
        ret.setFileName(rs.getString("filename"));
        ret.setFileTime(rs.getDate("filetime"));
        ret.setTotal(rs.getInt("total"));
        ret.setCreateDate(rs.getDate("createdate"));
        return ret;
      }
    }, fileName);
    if (list.size() > 0) {
      return list.get(0);
    } else {
      return null;
    }
  }
}
