package com.soagrowers.productview;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

/**
 * Created by ben on 19/01/16.
 */

@SpringBootApplication
public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String... args) {
        ApplicationContext context = SpringApplication.run(Application.class, args);
        LOG.debug("Starting App with ContextId:['{}']", context.getApplicationName(), context.getId());

        if(LOG.isTraceEnabled()) {
            String[] beans = context.getBeanDefinitionNames();
            Arrays.sort(beans);
            for (String bean : beans) {
                LOG.trace("ApplicationContext contains Bean [{}]", bean);
            }
        }
    }
}
