package com.hongyuecheng.login.controller;

import com.hongyuecheng.user.entity.User;
import com.hongyuecheng.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by admin on 2017/11/8.
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/toLogin", method = RequestMethod.GET)
    public String toLogin() {
        return "auth/toLogin";
    }

    /**
     * @param userName
     * @param password
     * @return
     */
    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    @ResponseBody
    public Object authUser(String userName, String password) {
        User user = userService.getUserByNameAndPassword(userName, password);

        if (null != user) {
            return user;
        } else {
            return false;
        }
    }

    @RequestMapping(value = "/main", method = RequestMethod.POST)
    public String main(User user, ModelAndView mv) {
        user = userService.getUserById(user.getId());
        mv.addObject("userInfo", user);
        return "auth/main";
    }
}
