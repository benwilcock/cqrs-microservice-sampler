package com.pankesh.productintegrationtests;


import com.pankesh.utils.Statics;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static com.jayway.restassured.RestAssured.given;


public class AssertSystemHealthTest {

    private static final Logger LOG = LoggerFactory.getLogger(AssertSystemHealthTest.class);

    private String productId = UUID.randomUUID().toString();


    @Before
    public void setup(){
        System.out.println("PRODUCTION MODE: " + Statics.PRODUCTION);
    }

    @Test
    public void assertGatewayHealth() {
        given().
                port(Statics.PORT_FOR_GATEWAY).
                when().
                get("/health/").
                then().
                statusCode(HttpStatus.SC_OK).
                body("status", Matchers.is("UP")).
                body("hystrix.status", Matchers.is("UP"));

        given().
                port(Statics.PORT_FOR_GATEWAY).
                when().
                get("/routes/").
                then().
                statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void assertDiscoveryHealth() {
        given().
                port(Statics.PORT_FOR_DISCOVERY).
                when().
                get("/health/").
                then().
                statusCode(HttpStatus.SC_OK).
                body("status", Matchers.is("UP")).
                body("discoveryComposite.status", Matchers.is("UP")).
                body("hystrix.status", Matchers.is("UP"));
    }

    @Test
    public void assertConfigHealth() {
        given().
                port(Statics.PORT_FOR_CONFIG).
                when().
                get("/health/").
                then().
                statusCode(HttpStatus.SC_OK).
                body("status", Matchers.is("UP")).
                body("configServer.status", Matchers.is("UP"));

        given().
                port(Statics.PORT_FOR_CONFIG).
                when().
                get("/integration-test/default/master").
                then().
                statusCode(HttpStatus.SC_OK).
                body("name", Matchers.is("integration-test"));
    }

    @Test
    public void assertCommandSideHealth() {

        String cmdConfigMessage;

        if(!Statics.PRODUCTION){
            cmdConfigMessage = Statics.LOCAL_CMD_MESSAGE;
        } else {
            cmdConfigMessage = Statics.PROD_CMD_MESSAGE;
        }

        given().
                port(Statics.PORT_FOR_GATEWAY).
                when().
                get("/commands/health/").
                then().
                statusCode(HttpStatus.SC_OK).
                body("status", Matchers.is("UP")).
                body("rabbit.status", Matchers.is("UP")).
                body("mongo.status", Matchers.is("UP"));

        given().
                port(Statics.PORT_FOR_GATEWAY).
                when().
                get("/commands/message").
                then().
                statusCode(HttpStatus.SC_OK).
                body(Matchers.is(cmdConfigMessage));

        given().
                port(Statics.PORT_FOR_GATEWAY).
                when().
                get("/commands/instances").
                then().
                statusCode(HttpStatus.SC_OK).
                body("serviceId", Matchers.hasItems(Statics.CMD_SERVICE_ID)).
                body("instanceInfo.actionType", Matchers.hasItems("ADDED"));
    }

    @Test
    public void assertQuerySideHealth() {

        String qryConfigMessage;
        if(!Statics.PRODUCTION){
            qryConfigMessage = Statics.LOCAL_QRY_MESSAGE;
        } else {
            qryConfigMessage = Statics.PROD_QRY_MESSAGE;
        }

        given().
                port(Statics.PORT_FOR_GATEWAY).
                when().
                get("/queries/health/").
                then().
                statusCode(HttpStatus.SC_OK).
                body("status", Matchers.is("UP")).
                body("db.status", Matchers.is("UP")).
                body("rabbit.status", Matchers.is("UP")).
                body("db.database", Matchers.is("H2"));

        given().
                port(Statics.PORT_FOR_GATEWAY).
                when().
                get("/queries/message").
                then().
                statusCode(HttpStatus.SC_OK).
                body(Matchers.is(qryConfigMessage));

        given().
                port(Statics.PORT_FOR_GATEWAY).
                when().
                get("/queries/instances").
                then().
                statusCode(HttpStatus.SC_OK).
                body("serviceId", Matchers.hasItems(Statics.QRY_SERVICE_ID)).
                body("instanceInfo.actionType", Matchers.hasItems("ADDED"));

    }
}
