package com.hongyuecheng.shop.controller;

import com.hongyuecheng.shop.entity.ShopInfo;
import com.hongyuecheng.shop.service.ShopInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by admin on 2017/11/14.
 */
@Controller
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private ShopInfoService shopInfoService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list() {
        return "shop/list";
    }

    @RequestMapping(value = "/getAllshops", method = RequestMethod.POST)
    @ResponseBody
    public List<ShopInfo> getAllshops(){
        return shopInfoService.getAllshops();
    }
}
