package com.ln.training.app.config;

import com.ln.training.app.common.constant.Constants;
import com.ln.training.app.common.util.CommonUtil;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;
import java.util.Objects;

@Configuration
public class ServletContextInitializerConfig implements ServletContextInitializer {

    @Override
    public void onStartup(ServletContext servletContext) {
        System.out.println("====> Khoi tao");
        System.setProperty(Constants.PROP_KEY_FOLDER,
                Objects.requireNonNull(CommonUtil.readProperties(Constants.PROP_KEY_FOLDER, "application.properties")));
    }
}
