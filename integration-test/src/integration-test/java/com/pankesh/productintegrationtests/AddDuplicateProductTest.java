package com.pankesh.productintegrationtests;

import org.apache.http.HttpStatus;
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
import static com.pankesh.utils.Statics.*;

/**
 * Created by ben on 09/03/16.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AddDuplicateProductTest {

    private static final Logger LOG = LoggerFactory.getLogger(AddDuplicateProductTest.class);
    private static String id;
    private static String name;

    @BeforeClass
    public static void setupClass() {
        id = UUID.randomUUID().toString();
        name = "Duplicate Testing Product [" + id + "]";
    }

    @After
    public void afterEach() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2l);
    }

    @Test
    public void testAddOfDuplicatesFailsPartA() {
        given().
                port(PORT_FOR_GATEWAY).
                when().
                post(PRODUCTS_CMD_BASE_PATH + CMD_PRODUCT_ADD + "/{id}?name={name}", id, name).
                then().
                statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    public void testAddOfDuplicatesFailsPartB() {
        given()
                .port(PORT_FOR_GATEWAY)
                .when()
                .post(PRODUCTS_CMD_BASE_PATH + CMD_PRODUCT_ADD + "/{id}?name={name}", id, name)
                .then()
                .statusCode(HttpStatus.SC_CONFLICT);
    }
}
