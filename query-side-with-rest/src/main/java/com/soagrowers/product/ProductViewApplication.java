package com.soagrowers.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Created by ben on 07/10/15.
 */
@SpringBootApplication
public class ProductViewApplication {

    private static ApplicationContext RestDataBootApplication;
    private static ApplicationContext RegularSpringApplication;
    private final static String CONTEXT_FILE_NAME = "queryContext.xml";

    public static void main(String[] args) {
        RestDataBootApplication = SpringApplication.run(ProductViewApplication.class, args);
        RegularSpringApplication = new ClassPathXmlApplicationContext(CONTEXT_FILE_NAME);
    }

    public static ApplicationContext getBootApplicationContext() {

        if (null != RestDataBootApplication) {
            return RestDataBootApplication;
        } else {
            throw new IllegalAccessError("No context to give you, sorry!");
        }
    }
}
