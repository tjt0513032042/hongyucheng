package com.hongyuecheng.utils;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

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
        int start = page.getPageNo() * page.getPageSize();
        // 防止超过最大页数
        if (start > page.getTotal()) {
            page.setPageNo(1);
            start = 0;
        }
        sql += " LIMIT " + start + "," + page.getPageSize();
        List result = jdbcTemplate.query(sql, rowMapper);
        page.setResult(result);
        return result;
    }
}
