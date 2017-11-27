package com.hongyuecheng.summary.entity;

import com.hongyuecheng.checkplan.entity.CheckPlan;
import com.hongyuecheng.checkplan.entity.CheckResult;
import com.hongyuecheng.report.entity.CheckRecords;
import com.hongyuecheng.shop.entity.ShopInfo;

import java.io.Serializable;

/**
 * Created by admin on 2017/11/26.
 */
public class SummaryBean implements Serializable {
    private CheckRecords openRecord;
    private boolean openFlag;
    private CheckRecords closeRecord;
    private boolean closeFlag;
    private ShopInfo shopInfo;
    private CheckPlan checkPlan;
    private String checkDate;
    private CheckResult checkResult;

    public CheckRecords getOpenRecord() {
        return openRecord;
    }

    public void setOpenRecord(CheckRecords openRecord) {
        this.openRecord = openRecord;
    }

    public CheckRecords getCloseRecord() {
        return closeRecord;
    }

    public void setCloseRecord(CheckRecords closeRecord) {
        this.closeRecord = closeRecord;
    }

    public ShopInfo getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(ShopInfo shopInfo) {
        this.shopInfo = shopInfo;
    }

    public CheckPlan getCheckPlan() {
        return checkPlan;
    }

    public void setCheckPlan(CheckPlan checkPlan) {
        this.checkPlan = checkPlan;
    }

    public String getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
    }

    public boolean isOpenFlag() {
        return openFlag;
    }

    public void setOpenFlag(boolean openFlag) {
        this.openFlag = openFlag;
    }

    public boolean isCloseFlag() {
        return closeFlag;
    }

    public void setCloseFlag(boolean closeFlag) {
        this.closeFlag = closeFlag;
    }

    public CheckResult getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(CheckResult checkResult) {
        this.checkResult = checkResult;
    }
}
