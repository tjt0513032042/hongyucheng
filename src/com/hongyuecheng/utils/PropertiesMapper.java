package com.hongyuecheng.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Created by admin on 2017/12/10.
 */
public class PropertiesMapper {
    private static Logger logger = LoggerFactory.getLogger(PropertiesMapper.class);

    private static ResourceBundle bundle;

    public static void loadData(String path) {
        bundle = ResourceBundle.getBundle(path, Locale.getDefault());
//        try {
//            InputStream in = new BufferedInputStream(new FileInputStream(path));
//            prop.load(in);
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("初始化配置文件失败,请检查");
//            logger.error("初始化配置文件失败,请检查");
//        }
    }

    public static String getValue(String key) {
        return bundle.getString(key);
    }
}
