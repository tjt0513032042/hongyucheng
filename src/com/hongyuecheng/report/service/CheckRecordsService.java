package com.hongyuecheng.report.service;

import java.util.Date;
import java.util.List;

import com.hongyuecheng.common.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hongyuecheng.checkplan.entity.CheckPlan;
import com.hongyuecheng.checkplan.service.CheckPlanService;
import com.hongyuecheng.report.dao.CheckRecordsDao;
import com.hongyuecheng.report.entity.CheckRecordDetail;
import com.hongyuecheng.report.entity.CheckRecords;
import com.hongyuecheng.shop.service.ShopInfoService;
import com.hongyuecheng.user.service.UserService;
import com.hongyuecheng.utils.DateUtil;
import com.hongyuecheng.utils.Page;

/**
 * Created by admin on 2017/11/25.
 */
@Service
public class CheckRecordsService {

    @Autowired
    private CheckRecordsDao checkRecordsDao;

    @Autowired
    private UserService userService;
    @Autowired
    private ShopInfoService shopInfoService;
    @Autowired
    private CheckRecordDetailService checkRecordDetailService;
    @Autowired
    private CheckPlanService checkPlanService;
    

    public void queryCheckRecords(Date start, Date end, String shopName, Page page) {
        checkRecordsDao.queryCheckRecords(start, end, shopName, page);
        if (CollectionUtils.isNotEmpty(page.getResult())) {
            List<CheckRecords> result = page.getResult();
            for (CheckRecords records : result) {
                initCheckRecords(records);
            }
        }
    }

    public CheckRecords getCheckRecords(Integer recordId) {
        CheckRecords checkRecords = checkRecordsDao.getCheckRecords(recordId);
        initCheckRecords(checkRecords);
        return checkRecords;
    }
    
    /**
     * 初始化内部信息
     *
     * @param records
     */
    public void initCheckRecords(CheckRecords records) {
        if (null == records) {
            return;
        }
        records.setUser(userService.getUserById(records.getUserId()));
        records.setShopInfo(shopInfoService.getShopInfo(records.getShopId()));
        records.setDetails(checkRecordDetailService.getDetailsByRecordId(records.getRecordId()));
        // 获取抽查结果情况
        CheckPlan checkPlan = checkPlanService.getPlanByDateAndShop(DateUtil.parse(DateUtil.format(records.getCheckDate(), DateUtil.FORMAT_TYPE_1), DateUtil.FORMAT_TYPE_1), records.getShopId());
        if (null != checkPlan) {
            records.setCheckFlag(true);
            records.setPlanId(checkPlan.getPlanId());
        } else {
            records.setCheckFlag(false);
        }
    }

    /**
     * 查询指定时间指定商家的开店/闭店表
     *
     * @param date
     * @param shopId
     * @param recordType
     * @return
     */
    public CheckRecords getCheckRecords(String date, Integer shopId, Integer recordType) {
        CheckRecords records = checkRecordsDao.getCheckRecords(date, shopId, recordType);
        initCheckRecords(records);
        if (null != records) {
            records.setCheckDateStr(DateUtil.format(records.getCheckDate()));
        }
        return records;
    }
    
    
    public int addCheckRecords(CheckRecords checkRecords) {
    	checkRecordsDao.addCheckRecords(checkRecords.getShopId(),
    			DateUtil.format(checkRecords.getCheckDate()),
    			checkRecords.getUserId(), checkRecords.getRecordType());
        checkRecords = checkRecordsDao.getCheckRecords(DateUtil.format(checkRecords.getCheckDate(), DateUtil.FORMAT_TYPE_1), checkRecords.getShopId(), checkRecords.getRecordType());
        return checkRecords.getRecordId();
    }

    public int updateCheckRecords(CheckRecords checkRecords){
        return checkRecordsDao.updateCheckRecords(checkRecords);
    }
}
