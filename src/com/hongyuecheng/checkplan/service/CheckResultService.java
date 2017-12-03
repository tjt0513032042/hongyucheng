package com.hongyuecheng.checkplan.service;

import com.hongyuecheng.checkplan.dao.CheckResultDao;
import com.hongyuecheng.checkplan.entity.CheckResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by admin on 2017/11/16.
 */
@Service
public class CheckResultService {

    @Autowired
    private CheckResultDao checkResultDao;

    public CheckResult getCheckResult(Integer planId, Integer shopId) {
        return checkResultDao.getCheckResult(planId, shopId);
    }
    
    
    public int addCheckResult(CheckResult checkResult)  {
        return checkResultDao.add(checkResult);
    }
    
    public int updateCheckResult(CheckResult checkResult)  {
    	if(null != getCheckResult(checkResult.getPlanId(), checkResult.getShopId())){
    		return checkResultDao.update(checkResult);
    	}else{
    		return checkResultDao.add(checkResult);
    	}
    }
}
