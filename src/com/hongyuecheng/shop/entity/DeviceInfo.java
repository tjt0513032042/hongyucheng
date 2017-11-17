package com.hongyuecheng.shop.entity;

import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by admin on 2017/11/16.
 */
public class DeviceInfo implements Serializable {
    private Integer deviceId;
    private Integer deviceType;
    private String deviceName;

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public static RowMapper<DeviceInfo> getDefaultRowHandler() {
        return new RowMapper<DeviceInfo>() {
            @Override
            public DeviceInfo mapRow(ResultSet resultSet, int i) throws SQLException {
                DeviceInfo deviceInfo = new DeviceInfo();
                deviceInfo.setDeviceId(resultSet.getInt("DEVICE_ID"));
                deviceInfo.setDeviceType(resultSet.getInt("DEVICE_TYPE"));
                deviceInfo.setDeviceName(resultSet.getString("DEVICE_NAME"));
                return deviceInfo;
            }
        };
    }
}
