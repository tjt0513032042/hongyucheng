package com.hongyuecheng.checkplan.dao;

import com.hongyuecheng.checkplan.entity.CheckResult;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by admin on 2017/11/16.
 */
@Repository
public class CheckResultDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public CheckResult getCheckResult(Integer planId, Integer shopId) {
        if (null == planId || null == shopId) {
            return null;
        }
        String sql = "SELECT * FROM check_result where plan_id = ? and shop_id = ?";
        List<CheckResult> list = jdbcTemplate.query(sql, new Object[]{planId, shopId}, CheckResult.getDefaultRowHandler());
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }
}
