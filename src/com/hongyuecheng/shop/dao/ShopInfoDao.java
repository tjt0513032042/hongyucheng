package com.hongyuecheng.shop.dao;

import com.hongyuecheng.shop.entity.ShopInfo;
import com.hongyuecheng.utils.DaoUtils;
import com.hongyuecheng.utils.Page;
import com.hongyuecheng.utils.ReturnValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/11/14.
 */
@Repository
public class ShopInfoDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DeviceDao deviceDao;

    public List<ShopInfo> getAllshops() {
        String sql = "select * from shop_info";
        return jdbcTemplate.query(sql, ShopInfo.getDefaultRowHander());
    }

    public ShopInfo getShopInfo(Integer shopId) {
        String sql = "select * from shop_info where shop_id = ?";
        List<ShopInfo> list = jdbcTemplate.query(sql, new Object[]{shopId}, ShopInfo.getDefaultRowHander());
        if (!CollectionUtils.isEmpty(list)) {
            ShopInfo shopInfo = list.get(0);
            shopInfo.setDeviceInfoList(deviceDao.getDevicesByIds(shopInfo.getRunningDevices()));
            return shopInfo;
        }
        return null;
    }

    public int add(ShopInfo shopInfo) {
        String sql = "insert into shop_info (shop_name, shop_type) values (?, ?)";
        return jdbcTemplate.update(sql, shopInfo.getShopName(), shopInfo.getShopType());
    }

    public int update(ShopInfo shopInfo) {
        String sql = "update shop_info set shop_name = ?, shop_type = ? where shop_id = ?";
        return jdbcTemplate.update(sql, shopInfo.getShopName(), shopInfo.getShopType(), shopInfo.getShopId());
    }

    public int delete(Integer shopId) {
        String sql = "delete from shop_info where shop_id = ?";
        return jdbcTemplate.update(sql, shopId);
    }

    public void queryPageList(ShopInfo shopInfo, Page page) {
        String sql = "select * from shop_info where 1=1 ";
        if (null != shopInfo) {
            if (null != shopInfo.getShopName()) {
                sql += " and shop_name like '%" + shopInfo.getShopName() + "%' ";
            }
            if (null != shopInfo.getShopType()) {
                sql += " and shop_type = " + shopInfo.getShopType() + " ";
            }
        }
        Long total = DaoUtils.getTotalNumber(sql, jdbcTemplate);
        page.setTotal(total);
        if (total > 0) {
            DaoUtils.getPageResult(sql, page, ShopInfo.getDefaultRowHander(), jdbcTemplate);
        } else {
            page.setResult(new ArrayList());
        }
    }
}
