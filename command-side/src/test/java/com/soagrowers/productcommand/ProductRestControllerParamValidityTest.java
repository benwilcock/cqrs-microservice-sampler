package com.soagrowers.productcommand;

import com.soagrowers.utils.Asserts;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by ben on 02/03/16.
 */
public class ProductRestControllerParamValidityTest {

    ProductRestController controller;
    MockHttpServletResponse mockHttpServletResponse;


    @Before
    public void setup(){
        Asserts.INSTANCE.setAssertsTo(true);
        controller = new ProductRestController();
        mockHttpServletResponse = new MockHttpServletResponse();
    }

    @Test
    public void testAddWithBadRequestParams(){

        controller.add(null, null, mockHttpServletResponse);
        assertTrue(mockHttpServletResponse.getStatus() == HttpServletResponse.SC_BAD_REQUEST);

        controller.add(UUID.randomUUID().toString(), null, new MockHttpServletResponse());
        assertTrue(mockHttpServletResponse.getStatus() == HttpServletResponse.SC_BAD_REQUEST);

        controller.add(UUID.randomUUID().toString(), "", new MockHttpServletResponse());
        assertTrue(mockHttpServletResponse.getStatus() == HttpServletResponse.SC_BAD_REQUEST);

        controller.add("", "", new MockHttpServletResponse());
        assertTrue(mockHttpServletResponse.getStatus() == HttpServletResponse.SC_BAD_REQUEST);

    }
}
