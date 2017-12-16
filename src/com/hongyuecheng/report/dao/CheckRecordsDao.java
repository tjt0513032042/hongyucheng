package com.hongyuecheng.report.dao;

import com.hongyuecheng.report.entity.CheckRecords;
import com.hongyuecheng.shop.dao.ShopInfoDao;
import com.hongyuecheng.shop.entity.ShopInfo;
import com.hongyuecheng.user.entity.User;
import com.hongyuecheng.utils.DaoUtils;
import com.hongyuecheng.utils.Page;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/11/25.
 */
@Repository
public class CheckRecordsDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ShopInfoDao shopInfoDao;

    public void queryCheckRecords(Date start, Date end, String shopName, Page page) {
        String sql = "select * from check_records where 1=1 ";
        List<Object> params = new ArrayList<>();
        if (null != start) {
            sql += " and check_date >= ? ";
            params.add(start);
        }
        if (null != end) {
            sql += " and check_date <= ? ";
            params.add(end);
        }
        List<ShopInfo> shopInfos = shopInfoDao.getShopInfoByName(shopName);
        if (CollectionUtils.isNotEmpty(shopInfos)) {
            String shopIds = "";
            for (ShopInfo shop : shopInfos) {
                shopIds += shop.getShopId() + ",";
            }
            if (shopIds.length() > 0) {
                shopIds = shopIds.substring(0, shopIds.length() - 1);
            }
            if (StringUtils.isNotEmpty(shopIds)) {
                sql += " and shop_id in (" + shopIds + ") ";
            }
        } else if (StringUtils.isNotEmpty(shopName)){
            sql += " and 1=2 ";
        }
        sql += " order by shop_id, check_date desc ";
        Long total = DaoUtils.getTotalNumber(sql, params.toArray(), jdbcTemplate);
        page.setTotal(total);
        if (total > 0) {
            DaoUtils.getPageResult(sql, params.toArray(), page, CheckRecords.getDefaultRowHandler(), jdbcTemplate);
        } else {
            page.setResult(new ArrayList());
        }
    }

    public CheckRecords getCheckRecords(Integer recordId) {
        if (null == recordId) {
            return null;
        }
        String sql = "select * from check_records where record_id = ?";
        List<CheckRecords> list = jdbcTemplate.query(sql, new Object[]{recordId}, CheckRecords.getDefaultRowHandler());
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }
    

    public CheckRecords getCheckRecords(String date, Integer shopId, Integer recordType) {
        String sql = "SELECT * FROM check_records WHERE check_date >= '" + date + " 00:00:00' AND check_date <= '" + date + " 23:59:59' AND shop_id = ? AND record_type = ?";
        List<CheckRecords> list = jdbcTemplate.query(sql, new Object[]{shopId, recordType}, CheckRecords.getDefaultRowHandler());
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }
    
    
    public int addCheckRecords(Integer shopId,String checkDate,Integer userId,Integer recordType) {
        String sql = "insert into check_records (shop_id, check_date, user_id, record_type) values (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, shopId, checkDate, userId, recordType);
    }

    public int updateCheckRecords(CheckRecords checkRecords){
        String sql = "update check_records set user_id = ?, check_date = now() where record_id = ?";
        return jdbcTemplate.update(sql, checkRecords.getUserId(), checkRecords.getRecordId());
    }
}
