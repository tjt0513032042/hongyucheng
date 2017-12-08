package com.hongyuecheng.checkplan.entity;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * Created by admin on 2017/11/16.
 */
public class CheckResult implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2919433866528372609L;
	private Integer planId;
    private Integer shopId;
    private Integer status;
    private String description;
    private String imageNames;

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageNames() {
        return imageNames;
    }

    public void setImageNames(String imageNames) {
        this.imageNames = imageNames;
    }

    public static RowMapper<CheckResult> getDefaultRowHandler() {
        return new RowMapper<CheckResult>() {
            @Override
            public CheckResult mapRow(ResultSet resultSet, int i) throws SQLException {
                CheckResult result = new CheckResult();
                result.setPlanId(resultSet.getInt("PLAN_ID"));
                result.setShopId(resultSet.getInt("SHOP_ID"));
                result.setStatus(resultSet.getInt("STATUS"));
                result.setDescription(resultSet.getString("DESCRIPTION"));
                result.setImageNames(resultSet.getString("IMAGE_NAMES"));
                return result;
            }
        };
    }
}
