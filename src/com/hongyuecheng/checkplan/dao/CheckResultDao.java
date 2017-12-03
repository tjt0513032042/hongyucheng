package com.hongyuecheng.checkplan.dao;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.hongyuecheng.checkplan.entity.CheckResult;

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
    
    
    public int add(CheckResult checkResult) {
        String sql = "insert into check_result (plan_id, shop_id, status, description,image_names) values (?, ?, ?, ?,?)";
        return jdbcTemplate.update(sql, checkResult.getPlanId(), checkResult.getShopId(), checkResult.getStatus(),
        		checkResult.getDescription(),checkResult.getImageNames());
    }
    
    public int update(CheckResult checkResult) {
        String sql = "update check_result set ";
        if (null != checkResult.getStatus()) {
        	sql += "status = " + checkResult.getStatus() ;
        }
        if (StringUtils.isNotEmpty(checkResult.getDescription())) {
        	sql += " ,description = '" + checkResult.getDescription()+"'";
        }
        if (StringUtils.isNotEmpty(checkResult.getImageNames())) {
        	sql += " ,image_names = '" + checkResult.getImageNames()+"'";
        }
        sql += " where plan_id = "+checkResult.getPlanId()+" and shop_id = ?";
        return jdbcTemplate.update(sql, checkResult.getShopId());
    }
}
