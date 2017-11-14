package com.hongyuecheng.user.entity;

import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by admin on 2017/11/7.
 */
public class User implements Serializable {


    private Integer id;
    private String name;
    private Integer role;
    private Boolean canCheck;
    private String phone;
    private String password;
    private Integer shopId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Boolean getCanCheck() {
        return canCheck;
    }

    public void setCanCheck(Boolean canCheck) {
        this.canCheck = canCheck;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public static RowMapper<User> getDefaultRowHander() {
        RowMapper<User> rowMapper = new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int i) throws SQLException {
                User user = new User();
                user.setId(rs.getInt("ID"));
                user.setName(rs.getString("NAME"));
                user.setRole(rs.getInt("ROLE"));
                user.setPhone(rs.getString("PHONE"));
                user.setCanCheck(rs.getBoolean("CAN_CHECK"));
                user.setShopId(rs.getInt("SHOP_ID"));
                user.setPassword(rs.getString("PASSWORD"));
                return user;
            }
        };
        return rowMapper;
    }
}
