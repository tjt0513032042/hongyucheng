package com.hongyuecheng.report.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.hongyuecheng.report.entity.Report;
import com.hongyuecheng.utils.DaoUtils;
import com.hongyuecheng.utils.Page;

/**
 * Created by admin on 2017/11/14.
 */
@Repository
public class ReportDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    
    public void queryReoprt(Date start, Date end,String shopName, Page<Object> page) {
        StringBuffer sql = new StringBuffer();
        sql.append("select r.id,r.shop_id,s.shop_name,r.check_id,o.option_name check_name,r.check_value,r.check_date from check_record r");
        sql.append(" left join shop_info s on r.shop_id =s.shop_id left join check_option_info o on r.check_id = o.option_id where 1=1 ");
        List<Object> params = new ArrayList<>();
        if (null != start) {
        	sql.append(" and r.check_date >= ? ");
            params.add(start);
        }
        if (null != end) {
        	sql.append(" and r.check_date <= ? ");
            params.add(end);
        }
        if (null != shopName) {
        	sql.append(" and s.shop_name like  '%"+shopName+"%' ");
        }
        
        
        sql.append(" order by r.check_date,s.shop_name,o.option_name ");
        Long total = DaoUtils.getTotalNumber(sql.toString(), params.toArray(), jdbcTemplate);
        page.setTotal(total);
        if (total > 0) {
            DaoUtils.getPageResult(sql.toString(), params.toArray(), page, Report.getDefaultRowHander(), jdbcTemplate);
        } else {
            page.setResult(new ArrayList<>());
        }
    }
}
