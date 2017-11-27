package com.hongyuecheng.shop.dao;

import com.hongyuecheng.shop.entity.DeviceInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/11/16.
 */
@Repository
public class DeviceDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<DeviceInfo> getDevicesByIds(String ids) {
        if (StringUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        String sql = "select * from device_info where device_id in (" + ids + ")";
        return jdbcTemplate.query(sql, DeviceInfo.getDefaultRowHandler());
    }

    public List<DeviceInfo> getAllDevices() {
        String sql = "select * from device_info order by device_type, device_id";
        return jdbcTemplate.query(sql, DeviceInfo.getDefaultRowHandler());
    }

}
