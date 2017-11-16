package com.hongyuecheng.checkplan.entity;

import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by admin on 2017/11/16.
 */
public class CheckPlan implements Serializable {

    private Integer planId;
    private Date checkDate;
    private String shopIds;

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public String getShopIds() {
        return shopIds;
    }

    public void setShopIds(String shopIds) {
        this.shopIds = shopIds;
    }

    public static RowMapper<CheckPlan> getDefaultRowHander() {
        return new RowMapper<CheckPlan>() {
            @Override
            public CheckPlan mapRow(ResultSet resultSet, int i) throws SQLException {
                CheckPlan plan = new CheckPlan();
                plan.setPlanId(resultSet.getInt("PLAN_ID"));
                plan.setCheckDate(resultSet.getDate("CHECK_DATE"));
                plan.setShopIds(resultSet.getString("SHOP_IDS"));
                return plan;
            }
        };
    }
}
