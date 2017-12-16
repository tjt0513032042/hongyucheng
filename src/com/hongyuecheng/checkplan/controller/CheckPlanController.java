package com.hongyuecheng.checkplan.controller;

import com.hongyuecheng.checkplan.entity.CheckPlan;
import com.hongyuecheng.checkplan.entity.SavePlans;
import com.hongyuecheng.checkplan.service.CheckPlanService;
import com.hongyuecheng.utils.DateUtil;
import com.hongyuecheng.utils.Page;
import com.hongyuecheng.utils.ReturnValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/11/16.
 */
@Controller
@RequestMapping("/checkPlan")
public class CheckPlanController {

    @Autowired
    private CheckPlanService checkPlanService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list() {
        return "checkPlan/list";
    }

    @RequestMapping(value = "/queryPlans", method = RequestMethod.POST)
    @ResponseBody
    public Page queryPlans(String shopName, String start, String end, Page page) {
        if (null == page) {
            page = new Page();
        }
        Date startDate = DateUtil.parse(start, DateUtil.FORMAT_TYPE_1);
        Date endDate = DateUtil.parse(end, DateUtil.FORMAT_TYPE_1);
        checkPlanService.queryPlans(shopName, startDate, endDate, page);
        return page;
    }

    /**
     * 临时生成执行计划
     *
     * @param start
     * @param end
     * @return
     */
    @RequestMapping(value = "/createPlans", method = RequestMethod.POST)
    @ResponseBody
    public ReturnValue createPlans(Date start, Date end) {
        ReturnValue returnValue = new ReturnValue();
        returnValue.setData(checkPlanService.createPlans(start, end));
        returnValue.setFlag(true);
        return returnValue;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public ReturnValue save(@RequestBody SavePlans savePlans) {
        checkPlanService.addPlans(savePlans.getPlans());
        ReturnValue returnValue = new ReturnValue();
        returnValue.setFlag(true);
        return returnValue;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ReturnValue deleteCheckPlan(Integer planId) {
        int result = checkPlanService.deletePlan(planId);
        ReturnValue returnValue = new ReturnValue();
        returnValue.setFlag(result > 0);
        if (!returnValue.getFlag()) {
            returnValue.setMsg("抽查计划删除失败!");
        }
        return returnValue;
    }

    @RequestMapping(value = "/getCheckPlan", method = RequestMethod.POST)
    @ResponseBody
    public CheckPlan getCheckPlan(Integer planId) {
        if (null == planId) {
            return null;
        }
        CheckPlan plan = checkPlanService.getCheckPlan(planId);
        return plan;
    }

    @RequestMapping(value = "/modifyCheckPlan", method = RequestMethod.POST)
    @ResponseBody
    public ReturnValue modifyCheckPlan(CheckPlan checkPlan) {
        int result = checkPlanService.modifyCheckPlan(checkPlan);

        ReturnValue returnValue = new ReturnValue();
        returnValue.setFlag(result > 0);
        if (!returnValue.getFlag()) {
            returnValue.setMsg("修改抽查计划失败");
        }
        return returnValue;
    }
}
