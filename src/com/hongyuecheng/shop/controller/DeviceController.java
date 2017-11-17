package com.hongyuecheng.shop.controller;

import com.hongyuecheng.shop.entity.DeviceInfo;
import com.hongyuecheng.shop.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by admin on 2017/11/16.
 */
@Controller
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @RequestMapping(value = "/getAllDevices", method = RequestMethod.POST)
    @ResponseBody
    public List<DeviceInfo> getAllDevices() {
        return deviceService.getAllDevices();
    }
}
