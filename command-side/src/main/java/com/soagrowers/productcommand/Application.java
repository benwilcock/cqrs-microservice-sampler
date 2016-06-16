package com.soagrowers.productcommand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

/**
 * Created by ben on 19/01/16.
 */
@EnableEurekaClient
@EnableSwagger2
@SpringBootApplication
public class Application {

    public static void main(String... args) {
        ApplicationContext context = SpringApplication.run(Application.class, args);
    }
}

@RestController
class ServiceInstanceRestController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Value("${spring.application.name}")
    private String appName;

    @RequestMapping("/instances")
    public List<ServiceInstance> serviceInstancesByApplicationName() {
        return this.discoveryClient.getInstances(appName);
    }
}


@RefreshScope
@RestController
class MessageRestController {

    @Value("${message}")
    private String message;

    @RequestMapping("/message")
    String getMessage() {
        return this.message;
    }
}
