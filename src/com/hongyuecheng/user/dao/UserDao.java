package com.hongyuecheng.user.dao;

import com.hongyuecheng.user.entity.User;
import com.hongyuecheng.utils.DaoUtils;
import com.hongyuecheng.utils.Page;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/11/7.
 */
@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public User getUserById(Integer id) {
        String sql = "select * from user_info where id = ?";
        List<User> list = jdbcTemplate.query(sql, new Object[]{id}, User.getDefaultRowHander());
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    public User getUserByNameAndPassword(String name, String password) {
        String sql = "select * from user_info where name = ? and password = ? and active = 'Y'";
        List<User> list = jdbcTemplate.query(sql, new Object[]{name, password}, User.getDefaultRowHander());
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    public void queryUserList(User entity, Page<User> page) {
        String sql = "select * from user_info t where 1=1 and active = 'Y' ";
        if (null != entity) {
            if (StringUtils.isNotEmpty(entity.getName())) {
                sql += " and t.name like '%" + entity.getName() + "%' ";
            }
            if (StringUtils.isNotEmpty(entity.getPhone())) {
                sql += " and t.phone like '%" + entity.getPhone() + "%' ";
            }
        }
        Long total = DaoUtils.getTotalNumber(sql, jdbcTemplate);
        page.setTotal(total);
        if (total > 0) {
            DaoUtils.getPageResult(sql, page, User.getDefaultRowHander(), jdbcTemplate);
        } else {
            page.setResult(new ArrayList<>());
        }
    }

    public int deleteUserById(Integer id) {
        String sql = "update user_info set active = 'N' where id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int add(User user) {
        String sql = "insert into user_info (name, role, phone, shop_id) values (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, user.getName(), user.getRole(), user.getPhone(), user.getShopId());
    }

    public int update(User user) {
        String sql = "update user_info set ";
        if (null != user.getRole()) {
            sql += " role = " + user.getRole() + ", ";
        }
        if (StringUtils.isNotEmpty(user.getPhone())) {
            sql += " phone = '" + user.getPhone() + "', ";
        }
        sql += " shop_id = " + user.getShopId() + ", ";
        if (StringUtils.isNotEmpty(user.getPassword())) {
            sql += " password = '" + user.getPassword() + "', ";
        }
        sql += " name = '" + user.getName() + "' ";
        sql += " where id = ? ";
        return jdbcTemplate.update(sql, user.getId());
    }

    public User checkPhoneExist(String phone) {
        String sql = "select * from user_info where phone = ? and active = 'Y'";
        List<User> list = jdbcTemplate.query(sql, User.getDefaultRowHander(), phone);
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    public User getUserByName(String name) {
        String sql = "select * from user_info where name = '" + name + "' and active = 'Y'";
        List<User> list = jdbcTemplate.query(sql, User.getDefaultRowHander());
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }
}
