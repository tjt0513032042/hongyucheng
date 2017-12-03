package com.hongyuecheng.checkplan.service;

import com.hongyuecheng.checkplan.dao.CheckOptionDao;
import com.hongyuecheng.checkplan.entity.CheckOption;
import com.hongyuecheng.report.entity.CheckRecordDetail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/11/24.
 */
@Service
public class CheckOptionService {

    @Autowired
    private CheckOptionDao checkOptionDao;

    public List<CheckOption> getCheckOptionsByType(Integer checkType, Integer optionType) {
        if (null == checkType || null == optionType) {
            return new ArrayList<>();
        }
        return checkOptionDao.getCheckOptionsByType(checkType, optionType);
    }
    
    public List<CheckRecordDetail> getCheckRecordDetailByType(Integer checkType, Integer optionType) {
        if (null == checkType) {
            return new ArrayList<>();
        }
        return checkOptionDao.getCheckRecordDetailByType(checkType, optionType);
    }
}
