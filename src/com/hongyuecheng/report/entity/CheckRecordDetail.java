package com.hongyuecheng.report.entity;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by admin on 2017/11/25.
 */
public class CheckRecordDetail implements Serializable {

    private Integer recordId;
    private String optionCode;
    private Integer optionResult;

    private String optionName;

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public String getOptionCode() {
        return optionCode;
    }

    public void setOptionCode(String optionCode) {
        this.optionCode = optionCode;
    }

    public Integer getOptionResult() {
        return optionResult;
    }

    public void setOptionResult(Integer optionResult) {
        this.optionResult = optionResult;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public static RowMapper<CheckRecordDetail> getDefaultRowHandler() {
        return new RowMapper<CheckRecordDetail>() {
            @Override
            public CheckRecordDetail mapRow(ResultSet resultSet, int i) throws SQLException {
                CheckRecordDetail detail = new CheckRecordDetail();
                detail.setRecordId(resultSet.getInt("RECORD_ID"));
                detail.setOptionCode(resultSet.getString("OPTION_CODE"));
                detail.setOptionResult(resultSet.getInt("OPTION_RESULT"));
                int count = resultSet.getMetaData().getColumnCount();
                for (int j = 1; j <= count; j++) {
                    if (StringUtils.equalsIgnoreCase(resultSet.getMetaData().getColumnName(j), "OPTION_NAME")) {
                        detail.setOptionName(resultSet.getString("OPTION_NAME"));
                    }
                }
                return detail;
            }
        };
    }
}
