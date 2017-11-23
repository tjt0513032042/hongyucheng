package com.hongyuecheng.checkplan.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2017/11/20.
 */
public class SavePlans implements Serializable {
    private List<CheckPlan> plans;

    public List<CheckPlan> getPlans() {
        return plans;
    }

    public void setPlans(List<CheckPlan> plans) {
        this.plans = plans;
    }
}
