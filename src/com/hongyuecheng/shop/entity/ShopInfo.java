package com.hongyuecheng.shop.entity;

import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by admin on 2017/11/14.
 */
public class ShopInfo implements Serializable{

    private Integer shopId;
    private Integer shopType;
    private String shopName;

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getShopType() {
        return shopType;
    }

    public void setShopType(Integer shopType) {
        this.shopType = shopType;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public static RowMapper<ShopInfo> getDefaultRowMapper(){
        return new RowMapper<ShopInfo>() {
            @Override
            public ShopInfo mapRow(ResultSet resultSet, int i) throws SQLException {
                ShopInfo info = new ShopInfo();
                info.setShopId(resultSet.getInt("SHOP_ID"));
                info.setShopName(resultSet.getString("SHOP_NAME"));
                info.setShopType(resultSet.getInt("SHOP_TYPE"));
                return info;
            }
        };
    }
}
