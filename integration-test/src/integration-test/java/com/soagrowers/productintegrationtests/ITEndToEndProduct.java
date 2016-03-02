package com.soagrowers.productintegrationtests;



import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.jayway.restassured.RestAssured.given;
import static com.soagrowers.productintegrationtests.Statics.*;

/**
 * Created by ben on 24/02/16.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ITEndToEndProduct {

    private static final Logger LOG = LoggerFactory.getLogger(ITEndToEndProduct.class);
    private static String id;
    private static String name;

    @BeforeClass
    public static void setupClass(){
        id = UUID.randomUUID().toString();
        name = "I am Product ["+id+"]";
    }

    @After
    public void afterEach() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2l);
    }

    /**
     * Send a command to the command-side to create a new Product.
     */
    @Test
    public void testA_PostAProduct() {

        given().
                port(PORT_FOR_COMMANDS).
        when().
                post(PRODUCTS_CMD_BASE_PATH + CMD_PRODUCT_ADD + "/{id}?name={name}", id, name).
        then().
                statusCode(HttpStatus.SC_CREATED);

    }
    /**
     *  Check that the new Product created event has arrived on the query-side and been
     *  made available for clients to view.
     */

    @Test
    public void testB_GetAProduct(){

        given().
                port(Statics.PORT_FOR_QUERIES).
        when().
                get(PRODUCTS_QRY_BASE_PATH + "/{id}", id).
        then().
                statusCode(HttpStatus.SC_OK).
                body("name", Matchers.is(name));
    }
}
