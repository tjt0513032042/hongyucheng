package com.hongyuecheng.checkplan.controller;

import com.hongyuecheng.checkplan.entity.CheckResult;
import com.hongyuecheng.checkplan.service.CheckResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by admin on 2017/11/16.
 */
@Controller
@RequestMapping("/checkResult")
public class CheckResultController {

    @Autowired
    private CheckResultService checkResultService;

    @RequestMapping(value = "/getCheckResult", method = RequestMethod.POST)
    @ResponseBody
    public CheckResult getCheckResult(Integer planId, Integer shopId) {
        return checkResultService.getCheckResult(planId, shopId);
    }
}
