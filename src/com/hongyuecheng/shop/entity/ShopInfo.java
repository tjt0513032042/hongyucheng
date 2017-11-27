package com.hongyuecheng.shop.entity;

import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by admin on 2017/11/14.
 */
public class ShopInfo implements Serializable {

    private String sNo;
    private String floor;
    private Integer shopId;
    private Integer shopType;
    private String shopName;

    private String firstPersonName;
    private String firstPersonPost;
    private String secondPersonName;
    private String secondPersonPost;
    private String closeShopBox;
    private boolean hasSpareKey;
    private String spareKeyBox;
    private String runningDevices;
    private String firstPersonPhone;
    private String secondPersonPhone;

    private List<DeviceInfo> deviceInfoList;

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getShopType() {
        return shopType;
    }

    public void setShopType(Integer shopType) {
        this.shopType = shopType;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getFirstPersonPost() {
        return firstPersonPost;
    }

    public void setFirstPersonPost(String firstPersonPost) {
        this.firstPersonPost = firstPersonPost;
    }

    public String getFirstPersonName() {
        return firstPersonName;
    }

    public void setFirstPersonName(String firstPersonName) {
        this.firstPersonName = firstPersonName;
    }

    public String getSecondPersonName() {
        return secondPersonName;
    }

    public void setSecondPersonName(String secondPersonName) {
        this.secondPersonName = secondPersonName;
    }

    public String getSecondPersonPost() {
        return secondPersonPost;
    }

    public void setSecondPersonPost(String secondPersonPost) {
        this.secondPersonPost = secondPersonPost;
    }

    public String getCloseShopBox() {
        return closeShopBox;
    }

    public void setCloseShopBox(String closeShopBox) {
        this.closeShopBox = closeShopBox;
    }

    public boolean isHasSpareKey() {
        return hasSpareKey;
    }

    public void setHasSpareKey(boolean hasSpareKey) {
        this.hasSpareKey = hasSpareKey;
    }

    public String getSpareKeyBox() {
        return spareKeyBox;
    }

    public void setSpareKeyBox(String spareKeyBox) {
        this.spareKeyBox = spareKeyBox;
    }

    public String getRunningDevices() {
        return runningDevices;
    }

    public void setRunningDevices(String runningDevices) {
        this.runningDevices = runningDevices;
    }

    public String getFirstPersonPhone() {
        return firstPersonPhone;
    }

    public void setFirstPersonPhone(String firstPersonPhone) {
        this.firstPersonPhone = firstPersonPhone;
    }

    public String getSecondPersonPhone() {
        return secondPersonPhone;
    }

    public void setSecondPersonPhone(String secondPersonPhone) {
        this.secondPersonPhone = secondPersonPhone;
    }

    public List<DeviceInfo> getDeviceInfoList() {
        return deviceInfoList;
    }

    public void setDeviceInfoList(List<DeviceInfo> deviceInfoList) {
        this.deviceInfoList = deviceInfoList;
    }

    public String getsNo() {
        return sNo;
    }

    public void setsNo(String sNo) {
        this.sNo = sNo;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public static RowMapper<ShopInfo> getDefaultRowHander() {
        return new RowMapper<ShopInfo>() {
            @Override
            public ShopInfo mapRow(ResultSet resultSet, int i) throws SQLException {
                ShopInfo info = new ShopInfo();
                info.setsNo(resultSet.getString("S_NO"));
                info.setFloor(resultSet.getString("FLOOR"));
                info.setShopId(resultSet.getInt("SHOP_ID"));
                info.setShopName(resultSet.getString("SHOP_NAME"));
                info.setShopType(resultSet.getInt("SHOP_TYPE"));
                info.setFirstPersonName(resultSet.getString("FIRST_PERSON_NAME"));
                info.setFirstPersonPost(resultSet.getString("FIRST_PERSON_POST"));
                info.setSecondPersonName(resultSet.getString("SECOND_PERSON_NAME"));
                info.setSecondPersonPost(resultSet.getString("SECOND_PERSON_POST"));
                info.setCloseShopBox(resultSet.getString("CLOSE_SHOP_BOX"));
                info.setHasSpareKey(resultSet.getBoolean("HAS_SPARE_KEY"));
                info.setSpareKeyBox(resultSet.getString("SPARE_KEY_BOX"));
                info.setRunningDevices(resultSet.getString("RUNNING_DEVICES"));
                info.setFirstPersonPhone(resultSet.getString("FIRST_PERSON_PHONE"));
                info.setSecondPersonPhone(resultSet.getString("SECOND_PERSON_PHONE"));
                return info;
            }
        };
    }
}
