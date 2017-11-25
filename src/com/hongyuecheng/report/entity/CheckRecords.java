package com.hongyuecheng.report.entity;

import com.hongyuecheng.shop.entity.ShopInfo;
import com.hongyuecheng.user.entity.User;
import com.hongyuecheng.utils.DateUtil;
import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/11/25.
 */
public class CheckRecords implements Serializable {

    private Integer recordId;
    private Integer shopId;
    private Date checkDate;
    private Integer userId;
    private Integer recordType;

    private String checkDateStr;
    private ShopInfo shopInfo;
    private User user;
    private List<CheckRecordDetail> details;
    /**
     * 是否有抽查记录
     */
    private boolean checkFlag;
    /**
     * 抽查计划id，可以与商家id结合查询抽查结果
     */
    private Integer planId;

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRecordType() {
        return recordType;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

    public ShopInfo getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(ShopInfo shopInfo) {
        this.shopInfo = shopInfo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CheckRecordDetail> getDetails() {
        return details;
    }

    public void setDetails(List<CheckRecordDetail> details) {
        this.details = details;
    }

    public boolean isCheckFlag() {
        return checkFlag;
    }

    public void setCheckFlag(boolean checkFlag) {
        this.checkFlag = checkFlag;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public String getCheckDateStr() {
        return checkDateStr;
    }

    public void setCheckDateStr(String checkDateStr) {
        this.checkDateStr = checkDateStr;
    }

    public static RowMapper<CheckRecords> getDefaultRowHandler() {
        return new RowMapper<CheckRecords>() {
            @Override
            public CheckRecords mapRow(ResultSet resultSet, int i) throws SQLException {
                CheckRecords info = new CheckRecords();
                info.setRecordId(resultSet.getInt("RECORD_ID"));
                info.setShopId(resultSet.getInt("SHOP_ID"));
                info.setCheckDate(resultSet.getTimestamp("CHECK_DATE"));
                info.setUserId(resultSet.getInt("USER_ID"));
                info.setRecordType(resultSet.getInt("RECORD_TYPE"));
                if (null != info.getCheckDate()) {
                    info.setCheckDateStr(DateUtil.format(info.getCheckDate()));
                }
                return info;
            }
        };
    }
}
