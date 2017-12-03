package com.hongyuecheng.checkplan.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.hongyuecheng.checkplan.entity.CheckOption;
import com.hongyuecheng.report.entity.CheckRecordDetail;

/**
 * Created by admin on 2017/11/24.
 */
@Repository
public class CheckOptionDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<CheckOption> getCheckOptionsByType(Integer checkType, Integer optionType) {
        String sql = "select * from check_option_info where check_type = ? and option_type = ? order by option_sort";
        return jdbcTemplate.query(sql, new Object[]{checkType, optionType}, CheckOption.getDefaultRowHandler());
    }
    
    
    public List<CheckRecordDetail> getCheckRecordDetailByType(Integer checkType, Integer optionType) {
    	//0:是，1:否
    	if(null != optionType){
    		String sql = "select '' RECORD_ID,OPTION_CODE,1 OPTION_RESULT from check_option_info where check_type = ? and option_type = ? order by check_type, option_type, option_sort";
    		return jdbcTemplate.query(sql, new Object[]{checkType, optionType}, CheckRecordDetail.getDefaultRowHandler());
    	}else{
    		String sql = "select '' RECORD_ID,OPTION_CODE,OPTION_NAME,1 OPTION_RESULT from check_option_info where check_type = ? order by check_type, option_type, option_sort";
    		return jdbcTemplate.query(sql, new Object[]{checkType}, CheckRecordDetail.getDefaultRowHandler());
    	}
    }
}
