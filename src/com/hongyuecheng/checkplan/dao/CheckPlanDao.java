package com.hongyuecheng.checkplan.dao;

import com.hongyuecheng.checkplan.entity.CheckPlan;
import com.hongyuecheng.shop.dao.ShopInfoDao;
import com.hongyuecheng.utils.DaoUtils;
import com.hongyuecheng.utils.Page;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/11/16.
 */
@Repository
public class CheckPlanDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ShopInfoDao shopInfoDao;

    public void queryPlans(Date start, Date end, Page page) {
        String sql = "select * from check_plan where 1=1 ";
        List<Object> params = new ArrayList<>();
        if (null != start) {
            sql += " and check_date >= ? ";
            params.add(start);
        }
        if (null != end) {
            sql += " and check_date <= ? ";
            params.add(end);
        }
        Long total = DaoUtils.getTotalNumber(sql, params.toArray(), jdbcTemplate);
        page.setTotal(total);
        if (total > 0) {
            DaoUtils.getPageResult(sql, params.toArray(), page, CheckPlan.getDefaultRowHander(), jdbcTemplate);
            List<CheckPlan> plans = page.getResult();
            for (CheckPlan plan : plans) {
                plan.setShopList(shopInfoDao.getShopInfos(plan.getShopIds()));
            }
        } else {
            page.setResult(new ArrayList<>());
        }
    }

    public CheckPlan getPlanByDate(Date date) {
        String sql = "select * from check_plan where check_date = ?";
        List<CheckPlan> plans = jdbcTemplate.query(sql, new Object[]{date}, CheckPlan.getDefaultRowHander());
        if (CollectionUtils.isNotEmpty(plans)) {
            return plans.get(0);
        }
        return null;
    }

    public int deletePlanByDate(Date date) {
        String sql = "delete from check_plan where check_date = ?";
        return jdbcTemplate.update(sql, date);
    }

    public int addCheckPlan(CheckPlan plan) {
        String sql = "insert into check_plan (check_date, shop_ids) VALUE (?, ?)";
        return jdbcTemplate.update(sql, plan.getCheckDate(), plan.getShopIds());
    }

    public int deletePlan(Integer planId) {
        String sql = "delete from check_plan where plan_id = ?";
        return jdbcTemplate.update(sql, planId);
    }

    public CheckPlan getCheckPlan(Integer planId) {
        String sql = "select * from check_plan where plan_id = ?";
        List<CheckPlan> plans = jdbcTemplate.query(sql, new Object[]{planId}, CheckPlan.getDefaultRowHander());
        if (CollectionUtils.isNotEmpty(plans)) {
            CheckPlan plan = plans.get(0);
            plan.setShopList(shopInfoDao.getShopInfos(plan.getShopIds()));
            return plan;
        }
        return null;
    }

    public int modifyCheckPlan(CheckPlan checkPlan) {
        String sql = "update check_plan set shop_ids = ? where plan_id = ?";
        return jdbcTemplate.update(sql, checkPlan.getShopIds(), checkPlan.getPlanId());
    }

    public CheckPlan getPlanByDateAndShop(Date date, Integer shopId) {
        String sql = "select * from check_plan where check_date = ? and concat(',', shop_ids, ',') like '%, " + shopId + " ,%'";
        List<CheckPlan> plans = jdbcTemplate.query(sql, new Object[]{date}, CheckPlan.getDefaultRowHander());
        if (CollectionUtils.isNotEmpty(plans)) {
            return plans.get(0);
        }
        return null;
    }
}
