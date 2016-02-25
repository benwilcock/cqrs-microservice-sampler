package com.soagrowers.productintegrationtests;


import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static com.jayway.restassured.RestAssured.given;

/**
 * Created by ben on 24/02/16.
 */
public class AssertSystemHealth {

    private static final Logger LOG = LoggerFactory.getLogger(AssertSystemHealth.class);

    private String productId = UUID.randomUUID().toString();

    @Test
    public void assertCommandSideHealth() {
        given().
                port(Statics.COMMAND_SIDE_MANAGEMENT_PORT_NUM).
                when().
                get("/health/").
                then().
                statusCode(HttpStatus.SC_OK).
                body("status", Matchers.is("UP")).
                body("rabbit.status", Matchers.is("UP")).
                body("mongo.status", Matchers.is("UP"));
    }

    @Test
    public void assertQuerySideHealth() {
        given().
                port(Statics.QUERY_SIDE_MANAGEMENT_PORT_NUM).
                when().
                get("/health/").
                then().
                statusCode(HttpStatus.SC_OK).
                body("status", Matchers.is("UP")).
                body("db.status", Matchers.is("UP")).
                body("rabbit.status", Matchers.is("UP")).
                body("db.database", Matchers.is("H2"));
    }
}
