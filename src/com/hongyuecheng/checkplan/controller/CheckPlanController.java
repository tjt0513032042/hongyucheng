package com.hongyuecheng.checkplan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by admin on 2017/11/16.
 */
@Controller
@RequestMapping("/checkPlan")
public class CheckPlanController {

    @RequestMapping(value = "checkPlan/list", method = RequestMethod.GET)
    public String list(){
        return "checkPlan/list";
    }
}
