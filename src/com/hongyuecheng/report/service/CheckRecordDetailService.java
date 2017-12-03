package com.hongyuecheng.report.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hongyuecheng.report.dao.CheckRecordDetailDao;
import com.hongyuecheng.report.entity.CheckRecordDetail;

/**
 * Created by admin on 2017/11/25.
 */
@Service
public class CheckRecordDetailService {

    @Autowired
    private CheckRecordDetailDao checkRecordDetailDao;

    public List<CheckRecordDetail> getDetailsByRecordId(Integer recordId) {
        return checkRecordDetailDao.getDetailsByRecordId(recordId);
    }

    public boolean getRecordStatus(Integer recordId) {
        return checkRecordDetailDao.getRecordStatus(recordId);
    }
    
    public int addCheckRecordDetail(CheckRecordDetail checkRecordDetail) {
    	return checkRecordDetailDao.addCheckRecordDetail(checkRecordDetail.getRecordId(),
    			checkRecordDetail.getOptionCode(), checkRecordDetail.getOptionResult());
    }
    
    public int updateCheckRecordDetail(CheckRecordDetail checkRecordDetail) {
    	return checkRecordDetailDao.updateCheckRecordDetail(checkRecordDetail);
    }
}
