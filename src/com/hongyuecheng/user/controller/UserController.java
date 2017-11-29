package com.hongyuecheng.user.controller;

import com.hongyuecheng.shop.service.ShopInfoService;
import com.hongyuecheng.user.entity.User;
import com.hongyuecheng.user.service.UserService;
import com.hongyuecheng.utils.Page;
import com.hongyuecheng.utils.ReturnValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/11/7.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ShopInfoService shopInfoService;

    @RequestMapping("/list")
    public String list() {
        return "user/list";
    }

    @RequestMapping("/queryUserList")
    @ResponseBody
    public Page queryUserList(User entity, Page<User> page) {
        if (null == page) {
            page = new Page();
        }
        userService.queryUserList(entity, page);
        return page;
    }

    @RequestMapping("/getUserInfo")
    @ResponseBody
    public User getUserInfo(Integer id) {
        if (null == id) {
            return null;
        }
        User user = userService.getUserById(id);
        if(null != user.getShopId()){
            user.setShop(shopInfoService.getShopInfo(user.getShopId()));
        }
        return user;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public boolean delete(Integer id) {
        return userService.deleteUserById(id);
    }

    @RequestMapping("/saveUserInfo")
    @ResponseBody
    public ReturnValue saveUserInfo(User user) {
        ReturnValue result = new ReturnValue();
        if (null == user) {
            result.setFlag(false);
            result.setMsg("用户信息错误,无法保存!");
            return result;
        }
        User exsitUser = userService.checkPhoneExist(user.getPhone());
        if (null != exsitUser) {
            if (null == user.getId() || user.getId().intValue() != exsitUser.getId().intValue()) {
                result.setFlag(false);
                result.setMsg("该手机已注册,无法重复注册!");
                return result;
            }
        }
        exsitUser = userService.getUserByName(user.getName());
        if (null != exsitUser) {
            if (null == user.getId() || user.getId().intValue() != exsitUser.getId().intValue()) {
                result.setFlag(false);
                result.setMsg("该用户名已注册,无法重复注册!");
                return result;
            }
        }

        int excuteResult = 0;
        if (user.getId() == null) {// 修改
            excuteResult = userService.addUser(user);
        } else {// 新增
            excuteResult = userService.updateUser(user);
        }
        result.setFlag(excuteResult > 0);
        if (!result.getFlag()) {
            result.setMsg("操作失败!");
        }
        return result;
    }
}
