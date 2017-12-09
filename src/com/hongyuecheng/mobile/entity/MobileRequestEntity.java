package com.hongyuecheng.mobile.entity;

import com.hongyuecheng.checkplan.entity.CheckOption;
import com.hongyuecheng.report.entity.CheckRecordDetail;

import java.util.List;

/**
 * Created by admin on 2017/12/9.
 */
public class MobileRequestEntity {

    private List<CheckRecordDetail> details;

    public List<CheckRecordDetail> getDetails() {
        return details;
    }

    public void setDetails(List<CheckRecordDetail> details) {
        this.details = details;
    }
}
