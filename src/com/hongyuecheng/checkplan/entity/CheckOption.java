package com.hongyuecheng.checkplan.entity;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by admin on 2017/11/24.
 */
public class CheckOption {

    private Integer optionId;
    private Integer optionType;
    private String optionName;
    private String optionCode;
    private Integer optionSort;
    private Integer checkType;

    public Integer getOptionId() {
        return optionId;
    }

    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }

    public Integer getOptionType() {
        return optionType;
    }

    public void setOptionType(Integer optionType) {
        this.optionType = optionType;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getOptionCode() {
        return optionCode;
    }

    public void setOptionCode(String optionCode) {
        this.optionCode = optionCode;
    }

    public Integer getOptionSort() {
        return optionSort;
    }

    public void setOptionSort(Integer optionSort) {
        this.optionSort = optionSort;
    }

    public Integer getCheckType() {
        return checkType;
    }

    public void setCheckType(Integer checkType) {
        this.checkType = checkType;
    }

    public static RowMapper<CheckOption> getDefaultRowHandler(){
        return new RowMapper<CheckOption>() {
            @Override
            public CheckOption mapRow(ResultSet resultSet, int i) throws SQLException {
                CheckOption option = new CheckOption();
                option.setOptionId(resultSet.getInt("OPTION_ID"));
                option.setOptionType(resultSet.getInt("OPTION_TYPE"));
                option.setOptionName(resultSet.getString("OPTION_NAME"));
                option.setOptionCode(resultSet.getString("OPTION_CODE"));
                option.setOptionSort(resultSet.getInt("OPTION_SORT"));
                option.setCheckType(resultSet.getInt("CHECK_TYPE"));
                return option;
            }
        };
    }
}
