package com.hongyuecheng.report.entity;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

/**
 * Created by admin on 2017/11/14.
 */
public class Report implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -5706789787656857601L;
	private Integer id;
	
	private Integer shopId;
    private String shopName;
    
    private Integer checkId;
    private String checkName;
    private Integer checkValue;
    private Date checkDate;

    
    
	
    public Integer getId() {
		return id;
	}




	public void setId(Integer id) {
		this.id = id;
	}




	public Integer getShopId() {
		return shopId;
	}




	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}




	public String getShopName() {
		return shopName;
	}




	public void setShopName(String shopName) {
		this.shopName = shopName;
	}




	public Integer getCheckId() {
		return checkId;
	}




	public void setCheckId(Integer checkId) {
		this.checkId = checkId;
	}




	public Integer getCheckValue() {
		return checkValue;
	}




	public void setCheckValue(Integer checkValue) {
		this.checkValue = checkValue;
	}




	public Date getCheckDate() {
		return checkDate;
	}




	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	

	public String getCheckName() {
		return checkName;
	}




	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}




	public static RowMapper<Report> getDefaultRowHander() {
        return new RowMapper<Report>() {
            @Override
            public Report mapRow(ResultSet resultSet, int i) throws SQLException {
            	Report plan = new Report();
            	plan.setId(resultSet.getInt("ID"));
            	plan.setShopId(resultSet.getInt("SHOP_ID"));
            	plan.setShopName(resultSet.getString("SHOP_NAME"));
            	
            	plan.setCheckId(resultSet.getInt("CHECK_ID"));
            	plan.setCheckName(resultSet.getString("CHECK_NAME"));
            	plan.setCheckValue(resultSet.getInt("CHECK_VALUE"));
                plan.setCheckDate(resultSet.getDate("CHECK_DATE"));
                return plan;
            }
        };
    }

}
