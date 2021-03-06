package com.hongyuecheng.report.dao;

import com.hongyuecheng.report.entity.CheckRecordDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/11/25.
 */
@Repository
public class CheckRecordDetailDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<CheckRecordDetail> getDetailsByRecordId(Integer recordId) {
        if (null == recordId) {
            return new ArrayList<>();
        }
        String sql = "SELECT d.*, i.check_type, i.option_type, i.option_sort, i.option_name FROM check_record_detail d LEFT JOIN check_option_info i ON d.option_code = i.option_code WHERE record_id = ? order by check_type, option_type, option_sort";
        return jdbcTemplate.query(sql, new Object[]{recordId}, CheckRecordDetail.getDefaultRowHandler());
    }

    public boolean getRecordStatus(Integer recordId) {
        boolean flag = true;
        String sql = "select count(*) from check_record_detail where record_id = ? and option_result = 1";
        Long num = jdbcTemplate.queryForObject(sql, Long.class, recordId);
        if (num > 0) {
            flag = false;
        }
        return flag;
    }
    
    public int addCheckRecordDetail(Integer recordId,String optionCode,Integer optionResult) {
        String sql = "insert into check_record_detail (record_id, option_code, option_result) values (?, ?, ?)";
        return jdbcTemplate.update(sql, recordId, optionCode, optionResult);
    }
    
    public int updateCheckRecordDetail(CheckRecordDetail checkRecordDetail) {
        String sql = "UPDATE check_record_detail SET option_result = ? WHERE record_id = ? AND option_code = ?";
        return jdbcTemplate.update(sql, checkRecordDetail.getOptionResult(), checkRecordDetail.getRecordId(), checkRecordDetail.getOptionCode());
    }
}
