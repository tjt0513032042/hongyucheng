package com.hongyuecheng.report.controller;

import java.util.Date;

import com.hongyuecheng.report.entity.CheckRecords;
import com.hongyuecheng.report.service.CheckRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hongyuecheng.report.service.ReportService;
import com.hongyuecheng.utils.DateUtil;
import com.hongyuecheng.utils.Page;

/**
 * Created by admin on 2017/11/14.
 */
@Controller
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;
    @Autowired
    private CheckRecordsService checkRecordsService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list() {
        return "report/list";
    }
    
    
    @RequestMapping(value = "/queryReports", method = RequestMethod.POST)
    @ResponseBody
    public Page queryReports(String start, String end, String shopName,Page page) {
        if (null == page) {
            page = new Page();
        }
        Date startDate = DateUtil.parse(start, DateUtil.FORMAT_TYPE_1);
        Date endDate = DateUtil.parse(end, DateUtil.FORMAT_TYPE_1);
        reportService.queryReoprt(startDate, endDate,shopName, page);
        return page;
    }

    @RequestMapping(value = "/queryCheckRecords", method = RequestMethod.POST)
    @ResponseBody
    public Page queryCheckRecords(String start, String end, String shopName,Page page) {
        if (null == page) {
            page = new Page();
        }
        Date startDate = DateUtil.parse(start, DateUtil.FORMAT_TYPE_1);
        Date endDate = DateUtil.parse(end, DateUtil.FORMAT_TYPE_1);
        checkRecordsService.queryCheckRecords(startDate, endDate,shopName, page);
        return page;
    }

    @RequestMapping(value = "/getCheckRecords", method = RequestMethod.POST)
    @ResponseBody
    public CheckRecords getCheckRecords(Integer recordId) {
        return checkRecordsService.getCheckRecords(recordId);
    }
}
