package com.soagrowers.productintegrationtests;


import com.soagrowers.utils.Statics;
import org.apache.http.HttpStatus;
import org.apache.http.protocol.HTTP;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static com.jayway.restassured.RestAssured.given;

/**
 * Created by ben on 24/02/16.
 */
public class AssertSystemHealthTest {

    private static final Logger LOG = LoggerFactory.getLogger(AssertSystemHealthTest.class);

    private String productId = UUID.randomUUID().toString();
    private String cmdConfigMessage;
    private String qryConfigMessage;

    @Before
    public void setup(){
        System.out.println("PRODUCTION MODE: " + Statics.PRODUCTION);
        if(!Statics.PRODUCTION){
            cmdConfigMessage = Statics.LOCAL_CMD_MESSAGE;
            qryConfigMessage = Statics.LOCAL_QRY_MESSAGE;
        } else {
            cmdConfigMessage = Statics.PROD_CMD_MESSAGE;
            qryConfigMessage = Statics.PROD_QRY_MESSAGE;
        }
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
                //body("$.propertySources[0].source.*", Matchers.hasItem("0eb10d28-32ff-11e6-8bd1-5775583ead59"));
                //body("propertySources.source", Matchers.hasValue("0eb10d28-32ff-11e6-8bd1-5775583ead59"));
    }

    @Test
    public void assertCommandSideHealth() {
        given().
                port(Statics.PORT_FOR_COMMANDS).
                when().
                get("/health/").
                then().
                statusCode(HttpStatus.SC_OK).
                body("status", Matchers.is("UP")).
                body("rabbit.status", Matchers.is("UP")).
                body("mongo.status", Matchers.is("UP"));

        given().
                port(Statics.PORT_FOR_COMMANDS).
                when().
                get("/message").
                then().
                statusCode(HttpStatus.SC_OK).
                body(Matchers.is(cmdConfigMessage));

        given().
                port(Statics.PORT_FOR_COMMANDS).
                when().
                get("/instances").
                then().
                statusCode(HttpStatus.SC_OK).
                body("serviceId", Matchers.hasItems(Statics.CMD_SERVICE_ID)).
                body("instanceInfo.actionType", Matchers.hasItems("ADDED"));
    }

    @Test
    public void assertQuerySideHealth() {
        given().
                port(Statics.PORT_FOR_QUERIES).
                when().
                get("/health/").
                then().
                statusCode(HttpStatus.SC_OK).
                body("status", Matchers.is("UP")).
                body("db.status", Matchers.is("UP")).
                body("rabbit.status", Matchers.is("UP")).
                body("db.database", Matchers.is("H2"));

        given().
                port(Statics.PORT_FOR_QUERIES).
                when().
                get("/message").
                then().
                statusCode(HttpStatus.SC_OK).
                body(Matchers.is(qryConfigMessage));

        given().
                port(Statics.PORT_FOR_QUERIES).
                when().
                get("/instances").
                then().
                statusCode(HttpStatus.SC_OK).
                body("serviceId", Matchers.hasItems(Statics.QRY_SERVICE_ID)).
                body("instanceInfo.actionType", Matchers.hasItems("ADDED"));

    }
}
