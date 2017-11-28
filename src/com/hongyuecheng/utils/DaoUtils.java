package com.hongyuecheng.utils;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by admin on 2017/11/12.
 */
public class DaoUtils {

    /**
     * 获得总数
     *
     * @param sql
     * @param jdbcTemplate
     * @return
     */
    public static Long getTotalNumber(String sql, JdbcTemplate jdbcTemplate) {
        String countSql = " select count(*) from (" + sql + ") countTempTableName";
        return jdbcTemplate.queryForObject(countSql, Long.class);
    }

    /**
     * 获得总条数
     *
     * @param sql
     * @param params
     * @param jdbcTemplate
     * @return
     */
    public static Long getTotalNumber(String sql, Object[] params, JdbcTemplate jdbcTemplate) {
        if (null == params) {
            return getTotalNumber(sql, jdbcTemplate);
        }
        String countSql = " select count(*) from (" + sql + ") countTempTableName";
        Long count = jdbcTemplate.queryForObject(countSql, params, Long.class);
        return count;
    }

    /**
     * 分页查询
     *
     * @param sql
     * @param page
     * @param rowMapper
     * @param jdbcTemplate
     * @return
     */
    public static List getPageResult(String sql, Page page, RowMapper rowMapper, JdbcTemplate jdbcTemplate) {
        if (StringUtils.isEmpty(sql) || null == page || null == rowMapper) {
            return null;
        }
        int start = getStart(page);
        sql += " LIMIT " + start + "," + page.getPageSize();
        List result = jdbcTemplate.query(sql, rowMapper);
        page.setResult(result);
        return result;
    }

    /**
     * 分页查询
     *
     * @param sql
     * @param params
     * @param page
     * @param rowMapper
     * @param jdbcTemplate
     * @return
     */
    public static List getPageResult(String sql, Object[] params, Page page, RowMapper rowMapper, JdbcTemplate jdbcTemplate) {
        if (null == params) {
            return getPageResult(sql, page, rowMapper, jdbcTemplate);
        }
        if (StringUtils.isEmpty(sql) || null == page || null == rowMapper) {
            return null;
        }
        int start = getStart(page);
        sql += " LIMIT " + start + "," + page.getPageSize();
        List result = jdbcTemplate.query(sql, params, rowMapper);
        page.setResult(result);
        return result;
    }

    private static int getStart(Page page) {
        int start = page.getPageNo() * page.getPageSize();
        // 防止超过最大页数
        if (start > page.getTotal()) {
            page.setPageNo(0);
            start = 0;
        }
        return start;
    }
}
