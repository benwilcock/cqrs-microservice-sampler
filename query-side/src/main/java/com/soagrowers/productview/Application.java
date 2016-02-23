package com.soagrowers.productview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;


/**
 * Created by ben on 07/10/15.
 */
@SpringBootApplication
@EntityScan("com.soagrowers.productview.domain")
public class Application {
    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }
}
