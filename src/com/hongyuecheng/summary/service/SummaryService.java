package com.hongyuecheng.summary.service;

import com.hongyuecheng.checkplan.service.CheckPlanService;
import com.hongyuecheng.checkplan.service.CheckResultService;
import com.hongyuecheng.common.Constants;
import com.hongyuecheng.report.service.CheckRecordDetailService;
import com.hongyuecheng.report.service.CheckRecordsService;
import com.hongyuecheng.shop.entity.ShopInfo;
import com.hongyuecheng.shop.service.ShopInfoService;
import com.hongyuecheng.summary.entity.SummaryBean;
import com.hongyuecheng.utils.DateUtil;
import com.hongyuecheng.utils.Page;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
            String checkDate = date + "-";
            if (i < 10) {
                checkDate += '0';
            }
            checkDate += i;
            SummaryBean summaryBean = getSummaryBean(checkDate, shopInfo);
            if (null != summaryBean) {
                result.add(summaryBean);
            }
        }
        page.setResult(result);
    }

    public void queryDayList(String date, Page page) {
        // 查询商家信息，用于分页
        shopInfoService.queryPageList(null, page);
        List<ShopInfo> shops = page.getResult();
        if (CollectionUtils.isNotEmpty(shops)) {
            List<SummaryBean> summaryBeanList = new ArrayList<>();
            for (ShopInfo shop : shops) {
                SummaryBean summaryBean = getSummaryBean(date, shop);
                summaryBeanList.add(summaryBean);
            }
            page.setResult(summaryBeanList);
        }
    }

    private SummaryBean getSummaryBean(String checkDate, ShopInfo shopInfo) {
        if (StringUtils.isEmpty(checkDate) || null == shopInfo || null == shopInfo.getShopId()) {
            return null;
        }
        SummaryBean summaryBean = new SummaryBean();
        summaryBean.setCheckDate(checkDate);
        summaryBean.setShopInfo(shopInfo);
        // 设置抽查结果
        summaryBean.setCheckPlan(checkPlanService.getPlanByDateAndShop(DateUtil.parse(checkDate, DateUtil.FORMAT_TYPE_1), shopInfo.getShopId()));
        if (null != summaryBean.getCheckPlan()) {
            summaryBean.setCheckResult(checkResultService.getCheckResult(summaryBean.getCheckPlan().getPlanId(), shopInfo.getShopId()));
        }
        // 查询开店闭店信息
        summaryBean.setOpenRecord(checkRecordsService.getCheckRecords(checkDate, shopInfo.getShopId(), Constants.CHECK_RECORD_TYPE_OPEN));
        if (null != summaryBean.getOpenRecord()) {
            summaryBean.setOpenFlag(checkRecordDetailService.getRecordStatus(summaryBean.getOpenRecord().getRecordId()));
        }
        summaryBean.setCloseRecord(checkRecordsService.getCheckRecords(checkDate, shopInfo.getShopId(), Constants.CHECK_RECORD_TYPE_CLOSE));
        if (null != summaryBean.getCloseRecord()) {
            summaryBean.setCloseFlag(checkRecordDetailService.getRecordStatus(summaryBean.getCloseRecord().getRecordId()));
        }
        return summaryBean;
    }
}
