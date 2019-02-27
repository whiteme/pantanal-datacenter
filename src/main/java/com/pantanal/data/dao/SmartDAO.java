package com.pantanal.data.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@Repository("smartDAO")
public class SmartDAO {


    protected final static Logger logger = LoggerFactory.getLogger(SmartDAO.class);

    @Autowired
    @Qualifier("xuwuJdbcTemplate")
    private JdbcTemplate jdbcTemplate;



    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    public static Logger getLogger() {
        return logger;
    }



    // 计算记录的条数
    public int getCount(String sql) throws Exception {
        logger.debug("[sql]=[" + sql + "]");
        return jdbcTemplate.queryForObject(sql , Integer.class);
    }

    // 计算记录的条数
    public int getCount(String sql, Object[] args) throws Exception {
        if (logger.isDebugEnabled()) {
            // log
            StringBuffer sb = new StringBuffer();
            sb.append(args[0]);
            int len = args.length;
            for (int i = 1; i < len; i++) {
                sb.append(", ").append(args[i]);
            }
            logger.debug("[sql]=[" + sql + "]\n[args]=[" + sb.toString() + "]");
        }
        return jdbcTemplate.queryForObject(sql, args , Integer.class);
    }

    public int update(String user, String sql, Object[] args) throws Exception {
        if (logger.isDebugEnabled()) {
            // log
            StringBuffer sb = new StringBuffer();
            sb.append(args[0]);
            int len = args.length;
            for (int i = 1; i < len; i++) {
                sb.append(",").append(args[i]);
            }
            logger.debug(user + " execute sql=[" + sql + "]\nargs=[" + sb.toString() + "]");
        }
        return jdbcTemplate.update(sql, args);
    }

    public int update(String user, String sql) throws Exception {
        logger.debug(user + " execute sql: " + sql);
        return jdbcTemplate.update(sql);
    }

    public int[] batchUpdate(String user, String sql, List<Object[]> batchArgs) throws Exception {
        if (logger.isDebugEnabled()) {
            StringBuffer sb0 = new StringBuffer();
            if (batchArgs != null && batchArgs.size() > 0) {
                for (Object[] args : batchArgs) {
                    sb0.append("{");
                    StringBuffer sb = new StringBuffer();
                    sb.append(args[0]);
                    int len = args.length;
                    for (int i = 1; i < len; i++) {
                        sb.append(",").append(args[i]);
                    }
                    sb0.append(sb.toString());
                    sb0.append("}");
                }
            }
            logger.debug(user + " execute sql=[" + sql + "]\nargs=[" + sb0.toString() + "]");
        }
        return jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    public <T> List<T> getList(Class<T> clazz, String sql) throws Exception {
        logger.debug("[sql]=[" + sql + "]");
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(clazz));
    }
    public <T> List<T> getList(Class<T> clazz, String sql, Map<String, String> args) throws Exception {
        logger.debug("[sql]=[" + sql + "]");
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(clazz), args);
    }

    public <T> List<T> getList(Class<T> clazz, String sql, Object[] args) throws Exception {
        if (logger.isDebugEnabled()) {
            // log
            StringBuffer sb = new StringBuffer();
            sb.append(args[0]);
            int len = args.length;
            for (int i = 1; i < len; i++) {
                sb.append(",").append(args[i]);
            }
            logger.debug("[sql]=[" + sql + "]\n[args]=[" + sb.toString() + "]");
        }
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(clazz), args);
    }

    public <T> T find(Class<T> clazz, String sql) throws Exception {
        try {
            logger.debug("[sql]=[" + sql + "]");
            return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(clazz));
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            throw e;
        }

    }

    public <T> T find(Class<T> clazz, String sql, Map<String, String> args) throws Exception {
        try {
            logger.debug("[sql]=[" + sql + "]");
            return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(clazz), args);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            throw e;
        }

    }

    public <T> T find(Class<T> clazz, String sql, Object[] args) throws Exception {
        try {
            if (logger.isDebugEnabled()) {
                // log
                StringBuffer sb = new StringBuffer();
                sb.append(args[0]);
                int len = args.length;
                for (int i = 1; i < len; i++) {
                    sb.append(",").append(args[i]);
                }
                logger.debug("[sql]=[" + sql + "]\n[args]=[" + sb.toString() + "]");
            }
            return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(clazz), args);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            throw e;
        }

    }

    public <T> List<T> getFields(Class<T> clazz, String sql, Object[] args, String columnName) throws Exception {
        try {
            if (logger.isDebugEnabled()) {
                // log
                StringBuffer sb = new StringBuffer();
                sb.append(args[0]);
                int len = args.length;
                for (int i = 1; i < len; i++) {
                    sb.append(",").append(args[i]);
                }
                logger.debug("[sql]=[" + sql + "]\n[args]=[" + sb.toString() + "]");
            }
            final String columnName2 = columnName;
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<T>() {

                @SuppressWarnings("unchecked")
                public T mapRow(ResultSet arg0, int arg1) throws SQLException {

                    return (T) arg0.getObject(columnName2);

                }

            }, args);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            throw e;
        }
    }

    public <T> List<T> getFields(Class<T> clazz, String sql, String columnName) throws Exception {
        try {

            final String columnName2 = columnName;
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<T>() {

                @SuppressWarnings("unchecked")
                public T mapRow(ResultSet arg0, int arg1) throws SQLException {
                    return (T) arg0.getObject(columnName2);
                }

            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            throw e;
        }
    }

}

