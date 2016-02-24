package com.soagrowers.productview;


import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static com.jayway.restassured.RestAssured.given;

/**
 * Created by ben on 24/02/16.
 */
public class EndToEndITest {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCommandDuplicatesTest.class);

    private String productId = UUID.randomUUID().toString();


    @Test
    public void getProducts() {
        LOG.debug("Port is set to: {}", 9090);
        given().
                port(9090).
                when().
                get("/products/").
                then().
                statusCode(200);
    }
}
