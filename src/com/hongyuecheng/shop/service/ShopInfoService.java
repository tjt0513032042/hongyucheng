package com.hongyuecheng.shop.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hongyuecheng.shop.dao.ShopInfoDao;
import com.hongyuecheng.shop.entity.ShopInfo;
import com.hongyuecheng.utils.Page;

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

    public int add(ShopInfo shopInfo) {
        return shopInfoDao.add(shopInfo);
    }

    public int update(ShopInfo shopInfo) {
        return shopInfoDao.update(shopInfo);
    }

    public int delete(Integer shopId) {
        return shopInfoDao.delete(shopId);
    }

    public void queryPageList(ShopInfo shopInfo, Page page) {
        shopInfoDao.queryPageList(shopInfo, page);
    }

    public ShopInfo getShopInfo(Integer shopId) {
        return shopInfoDao.getShopInfo(shopId);
    }

    public List<ShopInfo> getShopInfoRandom(int count) {
        return shopInfoDao.getShopInfoRandom(count);
    }
    public List<ShopInfo> getShopInfos(String shopIds) {
        return shopInfoDao.getShopInfos(shopIds);
    }

    public String getShopIds(List<ShopInfo> shopInfos) {
        String ids = "";
        if (CollectionUtils.isNotEmpty(shopInfos)) {
            for (ShopInfo shopInfo : shopInfos) {
                ids += shopInfo.getShopId() + ",";
            }
            if (ids.length() > 0) {
                ids = ids.substring(0, ids.length() - 1);
            }
        }
        return ids;
    }

    public List<ShopInfo> getShopInfoByName(String shopName) {
        return shopInfoDao.getShopInfoByName(shopName);
    }

    /**
     * 根据商家编号获取商家信息
     *
     * @param sNo
     * @return
     */
    public ShopInfo getShopInfoBySNo(String sNo) {
        return shopInfoDao.getShopInfoBySNo(sNo);
    }
}
