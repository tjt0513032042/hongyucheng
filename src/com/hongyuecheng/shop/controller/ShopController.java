package com.hongyuecheng.shop.controller;

import com.hongyuecheng.shop.entity.ShopInfo;
import com.hongyuecheng.shop.service.ShopInfoService;
import com.hongyuecheng.utils.Page;
import com.hongyuecheng.utils.ReturnValue;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
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
    public List<ShopInfo> getAllshops() {
        return shopInfoService.getAllshops();
    }

    @RequestMapping(value = "/getShopInfo", method = RequestMethod.POST)
    @ResponseBody
    public ShopInfo getShopInfo(Integer shopId) {
        if (null == shopId) {
            return null;
        }
        return shopInfoService.getShopInfo(shopId);
    }

    @RequestMapping(value = "/saveShopInfo", method = RequestMethod.POST)
    @ResponseBody
    public ReturnValue saveShopInfo(ShopInfo shopInfo) {
        ReturnValue returnValue = new ReturnValue();
        if (null == shopInfo) {
            returnValue.setFlag(false);
            returnValue.setMsg("参数错误,无法保存!");
            return returnValue;
        }
        int excuteResult = 0;
        if (null == shopInfo.getShopId()) {// 新增
            excuteResult = shopInfoService.add(shopInfo);
        } else {// 修改
            excuteResult = shopInfoService.update(shopInfo);
        }
        returnValue.setFlag(excuteResult > 0);
        if (!returnValue.getFlag()) {
            returnValue.setMsg("操作失败!");
        }
        return returnValue;
    }

    @RequestMapping(value = "/deleteShopInfo", method = RequestMethod.POST)
    @ResponseBody
    public ReturnValue deleteShopInfo(Integer shopId) {
        ReturnValue returnValue = new ReturnValue();
        if (null == shopId) {
            returnValue.setFlag(false);
            returnValue.setMsg("参数错误,无法保存!");
            return returnValue;
        }
        shopInfoService.delete(shopId);
        returnValue.setFlag(true);
        return returnValue;
    }

    @RequestMapping(value = "/queryPageList", method = RequestMethod.POST)
    @ResponseBody
    public Page queryPageList(ShopInfo shopInfo, Page page) {
        if (null == page) {
            page = new Page();
        }
        shopInfoService.queryPageList(shopInfo, page);
        return page;
    }

    @RequestMapping(value = "/getShopInfoByName", method = RequestMethod.POST)
    @ResponseBody
    public List<ShopInfo> getShopInfoByName(String shopName) {
        return shopInfoService.getShopInfoByName(shopName);
    }
}
