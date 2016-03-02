package com.soagrowers.productcommand;

import com.soagrowers.productcommand.utils.Asserts;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;

import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by ben on 02/03/16.
 */
public class ProductRestControllerTest {

    ProductRestController controller;


    @Before
    public void setup(){
        Asserts.setAssertsTo(true);
        controller = new ProductRestController();
    }

    @Test
    public void testAddWithBadRequestParams(){

        try {
            controller.add(null, null, new MockHttpServletResponse());
            assertTrue(false);
        } catch (Throwable e){
            assertEquals(Asserts.UNEXPECTED_NULL, e.getMessage());
        }

        try {
            controller.add(UUID.randomUUID().toString(), null, new MockHttpServletResponse());
            assertTrue(false);
        } catch (Throwable ae){
            assertEquals(Asserts.UNEXPECTED_NULL, ae.getMessage());
        }

        try {
            controller.add(UUID.randomUUID().toString(), "", new MockHttpServletResponse());
            assertTrue(false);
        } catch (Throwable ae){
            assertEquals(Asserts.UNEXPECTED_EMPTY_STRING, ae.getMessage());
        }

        try {
            controller.add("", "", new MockHttpServletResponse());
            assertTrue(false);
        } catch (Throwable ae){
            assertEquals(Asserts.UNEXPECTED_NULL, ae.getMessage());
        }
    }
}
