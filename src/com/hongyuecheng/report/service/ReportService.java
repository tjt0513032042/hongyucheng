package com.hongyuecheng.report.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hongyuecheng.report.dao.ReportDao;
import com.hongyuecheng.utils.Page;


/**
 * Created by admin on 2017/11/14.
 */
@Service
public class ReportService {

    @Autowired
    private ReportDao reportDao;


    public void queryReoprt(Date start, Date end, String shopName, Page<Object> page) {
        reportDao.queryReoprt(start, end, shopName, page);
    }
}
