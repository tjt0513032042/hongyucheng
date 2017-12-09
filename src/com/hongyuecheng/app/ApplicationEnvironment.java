package com.hongyuecheng.app;

import com.hongyuecheng.utils.PropertiesMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 * Created by admin on 2017/12/10.
 */
public class ApplicationEnvironment implements InitializingBean, ServletContextAware {
    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        String propertiesFileName = "hongyuecheng";
        PropertiesMapper.loadData(propertiesFileName);
    }
}
