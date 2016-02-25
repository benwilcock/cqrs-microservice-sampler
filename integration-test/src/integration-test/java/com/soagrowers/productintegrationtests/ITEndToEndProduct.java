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
        name = "I'm Product ["+id+"]";
    }

    @After
    public void afterEach() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2l);
    }
    //http://localhost:9000/products/add/0000001?name=Awesome%20Drill

    /**
     * Send a command to the command-side to create a new Product.
     */
    @Test
    public void testA_PostAProduct() {

        given().
                port(PORT_FOR_COMMANDS).
        when().
                post(PRODUCTS + CMD_PRODUCT_ADD + "/{id}?name={name}", id, name).
        then().
                statusCode(HttpStatus.SC_OK);

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
                get(PRODUCTS+"/{id}", id).
        then().
                statusCode(HttpStatus.SC_OK).
                body("name", Matchers.is(name));
    }
}
