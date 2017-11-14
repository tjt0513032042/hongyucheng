package com.hongyuecheng.user.controller;

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

    @RequestMapping("/list")
    public String list() {
        User user = userService.getUserById(1);
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
        int excuteResult = 0;
        if (user.getId() == null) {// 修改
            excuteResult = userService.updateUser(user);
        } else {// 新增
            excuteResult = userService.addUser(user);
        }
        result.setFlag(excuteResult > 0);
        if (!result.getFlag()) {
            result.setMsg("操作失败!");
        }
        return result;
    }
}
