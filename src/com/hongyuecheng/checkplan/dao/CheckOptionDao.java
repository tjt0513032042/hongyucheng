package com.hongyuecheng.checkplan.dao;

import com.hongyuecheng.checkplan.entity.CheckOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
