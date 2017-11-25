package com.hongyuecheng.report.service;

import com.hongyuecheng.report.dao.CheckRecordDetailDao;
import com.hongyuecheng.report.entity.CheckRecordDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
