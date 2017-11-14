package com.hongyuecheng.shop.service;

import com.hongyuecheng.shop.dao.ShopInfoDao;
import com.hongyuecheng.shop.entity.ShopInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by admin on 2017/11/14.
 */
@Service
public class ShopInfoService {

    @Autowired
    private ShopInfoDao shopInfoDao;

    public List<ShopInfo> getAllshops() {
        return shopInfoDao.getAllshops();
    }
}
