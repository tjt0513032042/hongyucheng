package com.hongyuecheng.shop.dao;

import com.hongyuecheng.shop.entity.ShopInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by admin on 2017/11/14.
 */
@Repository
public class ShopInfoDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ShopInfo> getAllshops() {
        String sql = "select * from shop_info";
        return jdbcTemplate.query(sql, ShopInfo.getDefaultRowMapper());
    }
}
