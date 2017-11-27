package com.hongyuecheng.summary.controller;

import com.hongyuecheng.summary.service.SummaryService;
import com.hongyuecheng.utils.DateUtil;
import com.hongyuecheng.utils.Page;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * Created by admin on 2017/11/26.
 */
@Controller
@RequestMapping("/summary")
public class SummaryController {

    @Autowired
    private SummaryService summaryService;

    @RequestMapping(value = "/monthlist", method = RequestMethod.GET)
    public String monthList() {
        return "summary/monthlist";
    }

    @RequestMapping(value = "/daylist", method = RequestMethod.GET)
    public String dayList() {
        return "summary/daylist";
    }

    @RequestMapping(value = "/queryMonthList", method = RequestMethod.POST)
    @ResponseBody
    public Page queryMonthList(Integer shopId, String date, Page page) {
        if (null == page) {
            page = new Page();
        }
        Date month = DateUtil.parse(date, DateUtil.FORMAT_TYPE_3);
        if (null != shopId && null != month) {
            summaryService.queryMonthList(shopId, date, page);
        }
        return page;
    }
}
