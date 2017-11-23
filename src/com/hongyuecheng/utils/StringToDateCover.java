package com.hongyuecheng.utils;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

/**
 * Created by admin on 2017/11/20.
 */
public class StringToDateCover implements Converter<String, Date> {
    @Override
    public Date convert(String s) {
        Date result = null;
        if (StringUtils.isNotEmpty(s)) {
            if (s.length() == 10 && s.indexOf(":") == -1) {
                result = DateUtil.parse(s, DateUtil.FORMAT_TYPE_1);
            } else {
                result = DateUtil.parse(s);
            }
        }
        return result;
    }
}
