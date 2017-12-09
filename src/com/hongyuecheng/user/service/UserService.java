package com.hongyuecheng.user.service;

import com.hongyuecheng.shop.service.ShopInfoService;
import com.hongyuecheng.user.dao.UserDao;
import com.hongyuecheng.user.entity.User;
import com.hongyuecheng.utils.Page;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by admin on 2017/11/7.
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ShopInfoService shopInfoService;

    public User getUserById(Integer id) {
        if (null == id) {
            return null;
        }
        return userDao.getUserById(id);
    }

    public User getUserByNameAndPassword(String name, String password) {
        return userDao.getUserByNameAndPassword(name, password);
    }

    public User getUserByName(String name) {
        return userDao.getUserByName(name);
    }


    public void queryUserList(User entity, Page<User> page) {
        userDao.queryUserList(entity, page);
        List<User> list = page.getResult();
        if (CollectionUtils.isNotEmpty(list)) {
            for (User user : list) {
                if (null != user.getShopId()) {
                    user.setShop(shopInfoService.getShopInfo(user.getShopId()));
                }
            }
        }
    }

    public boolean deleteUserById(Integer id) {
        int result = userDao.deleteUserById(id);
        if (result > 0) {
            return true;
        }
        return false;
    }

    public int addUser(User user) {
        return userDao.add(user);
    }

    public int updateUser(User user) {
        return userDao.update(user);
    }

    public User checkPhoneExist(String phone) {
        return userDao.checkPhoneExist(phone);
    }
}
