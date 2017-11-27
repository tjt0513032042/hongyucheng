package com.hongyuecheng.summary.service;

import com.hongyuecheng.checkplan.service.CheckPlanService;
import com.hongyuecheng.checkplan.service.CheckResultService;
import com.hongyuecheng.common.Constants;
import com.hongyuecheng.report.entity.CheckRecords;
import com.hongyuecheng.report.service.CheckRecordDetailService;
import com.hongyuecheng.report.service.CheckRecordsService;
import com.hongyuecheng.shop.entity.ShopInfo;
import com.hongyuecheng.shop.service.ShopInfoService;
import com.hongyuecheng.summary.entity.SummaryBean;
import com.hongyuecheng.utils.DateUtil;
import com.hongyuecheng.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/11/26.
 */
@Service
public class SummaryService {

    @Autowired
    private ShopInfoService shopInfoService;
    @Autowired
    private CheckRecordsService checkRecordsService;
    @Autowired
    private CheckRecordDetailService checkRecordDetailService;
    @Autowired
    private CheckPlanService checkPlanService;
    @Autowired
    private CheckResultService checkResultService;

    public void queryMonthList(Integer shopId, String date, Page page) {
        List<SummaryBean> result = new ArrayList<>();
        ShopInfo shopInfo = shopInfoService.getShopInfo(shopId);
        // 根据月份查询出该月所有的天数
        int days = DateUtil.getDaysOfMonth(date);
        for (int i = 1; i <= days; i++) {
            String checkDate = date + "-" + i;

            SummaryBean summaryBean = new SummaryBean();
            summaryBean.setCheckDate(checkDate);
            summaryBean.setShopInfo(shopInfo);
            // 设置抽查结果
            summaryBean.setCheckPlan(checkPlanService.getPlanByDateAndShop(DateUtil.parse(checkDate, DateUtil.FORMAT_TYPE_1), shopId));
            if (null != summaryBean.getCheckPlan()) {
                summaryBean.setCheckResult(checkResultService.getCheckResult(summaryBean.getCheckPlan().getPlanId(), shopId));
            }
            // 查询开店闭店信息
            summaryBean.setOpenRecord(checkRecordsService.getCheckRecords(checkDate, shopId, Constants.CHECK_RECORD_TYPE_OPEN));
            if (null != summaryBean.getOpenRecord()) {
                summaryBean.setOpenFlag(checkRecordDetailService.getRecordStatus(summaryBean.getOpenRecord().getRecordId()));
            }
            summaryBean.setCloseRecord(checkRecordsService.getCheckRecords(checkDate, shopId, Constants.CHECK_RECORD_TYPE_CLOSE));
            if (null != summaryBean.getOpenRecord()) {
                summaryBean.setCloseFlag(checkRecordDetailService.getRecordStatus(summaryBean.getCloseRecord().getRecordId()));
            }
            result.add(summaryBean);
        }
        page.setResult(result);
    }
}
