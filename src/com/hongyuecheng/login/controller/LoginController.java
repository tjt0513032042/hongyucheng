package com.hongyuecheng.login.controller;

import com.hongyuecheng.common.Constants;
import com.hongyuecheng.user.entity.User;
import com.hongyuecheng.user.service.UserService;
import com.hongyuecheng.utils.ReturnValue;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
    public Object authUser(String userName, String password, String type, HttpSession session) {
        ReturnValue returnValue = new ReturnValue();
        User user = userService.getUserByNameAndPassword(userName, password);
        if (null != user) {
            if (StringUtils.equals(type, Constants.LOGIN_TYPE_PC)) {
                if (user.getRole().intValue() != Constants.USER_TYPE_ADMIN) {
                    returnValue.setFlag(false);
                    returnValue.setMsg("该用户仅能在手机端访问!");
                    return returnValue;
                }
            } else if (StringUtils.equals(type, Constants.LOGIN_TYPE_MOBILE)) {

            } else {
                returnValue.setFlag(false);
                returnValue.setMsg("非法登录");
                return returnValue;
            }
            session.setAttribute("user", user);
            returnValue.setFlag(true);
            returnValue.setData(user);
        } else {
            returnValue.setFlag(false);
            returnValue.setMsg("用户名或密码错误,无法登录!");
        }
        return returnValue;
    }

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String main(User user, ModelAndView mv, HttpServletRequest request) {
//        user = userService.getUserById(user.getId());
//        request.getSession().setAttribute("user", user);
//        mv.addObject("userInfo", user);
        return "auth/main";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public void logout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
    }
}
