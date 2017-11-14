package com.hongyuecheng.user.service;

import com.hongyuecheng.user.dao.UserDao;
import com.hongyuecheng.user.entity.User;
import com.hongyuecheng.utils.Page;
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

    public User getUserById(Integer id) {
        return userDao.getUserById(id);
    }

    public User getUserByNameAndPassword(String name, String password) {
        return userDao.getUserByNameAndPassword(name, password);
    }

    public void queryUserList(User entity, Page<User> page) {
        userDao.queryUserList(entity, page);
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
}
