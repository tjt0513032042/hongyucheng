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
        String sql = "SELECT d.*, i.option_sort, i.option_name FROM check_record_detail d LEFT JOIN check_option_info i ON d.option_code = i.option_code WHERE record_id = ? ORDER BY option_sort";
        return jdbcTemplate.query(sql, new Object[]{recordId}, CheckRecordDetail.getDefaultRowHandler());
    }
}
