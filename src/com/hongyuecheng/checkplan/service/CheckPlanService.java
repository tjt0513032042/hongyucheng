package com.hongyuecheng.checkplan.service;

import com.hongyuecheng.checkplan.dao.CheckPlanDao;
import com.hongyuecheng.checkplan.entity.CheckPlan;
import com.hongyuecheng.shop.entity.ShopInfo;
import com.hongyuecheng.shop.service.ShopInfoService;
import com.hongyuecheng.utils.DateUtil;
import com.hongyuecheng.utils.Page;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/11/16.
 */
@Service
public class CheckPlanService {

    @Autowired
    private ShopInfoService shopInfoService;

    @Autowired
    private CheckPlanDao checkPlanDao;

    public void queryPlans(Date start, Date end, Page page) {
        checkPlanDao.queryPlans(start, end, page);
    }

    public List<CheckPlan> createPlans(Date start, Date end) {
        List<CheckPlan> plans = new ArrayList<>();
        Date temp = start;
        CheckPlan plan = null;
        while (temp.getTime() <= end.getTime()) {
            plan = new CheckPlan();
            plan.setCheckDate(temp);
            // 添加被抽查的商店
            List<ShopInfo> shops = shopInfoService.getShopInfoRandom(3);
            plan.setShopList(shops);
            plan.setShopIds(shopInfoService.getShopIds(shops));
            plans.add(plan);

            temp = DateUtil.addDays(temp, 1);
        }
        return plans;
    }

    public void addPlans(List<CheckPlan> plans) {
        if (CollectionUtils.isNotEmpty(plans)) {
            for (CheckPlan plan : plans) {
                // 格式化日期为年月日
                plan.setCheckDate(DateUtil.parse(DateUtil.format(plan.getCheckDate(), DateUtil.FORMAT_TYPE_1), DateUtil.FORMAT_TYPE_1));
                if (null != getPlanByDate(plan.getCheckDate())) {
                    deletePlanByDate(plan.getCheckDate());
                }
                checkPlanDao.addCheckPlan(plan);
            }
        }
    }

    public CheckPlan getPlanByDate(Date date) {
        if (null == date) {
            return null;
        }
        return checkPlanDao.getPlanByDate(date);
    }

    public int deletePlanByDate(Date date) {
        if (null == date) {
            return 0;
        }
        return checkPlanDao.deletePlanByDate(date);
    }

    public int deletePlan(Integer planId) {
        if (null == planId) {
            return 0;
        }
        return checkPlanDao.deletePlan(planId);
    }

    public CheckPlan getCheckPlan(Integer planId) {
        return checkPlanDao.getCheckPlan(planId);
    }

    public int modifyCheckPlan(CheckPlan checkPlan) {
        if (null == checkPlan || null == checkPlan.getPlanId()) {
            return 0;
        }
        return checkPlanDao.modifyCheckPlan(checkPlan);
    }
}
