package com.hongyuecheng.shop.service;

import com.hongyuecheng.shop.dao.DeviceDao;
import com.hongyuecheng.shop.entity.DeviceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by admin on 2017/11/16.
 */
@Service
public class DeviceService {

    @Autowired
    private DeviceDao deviceDao;

    public List<DeviceInfo> getAllDevices() {
        return deviceDao.getAllDevices();
    }
}
