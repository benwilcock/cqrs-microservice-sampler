package com.pankesh.productcommand;

import com.pankesh.utils.Asserts;
import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class ProductRestControllerParamValidityTest {

    ProductRestController controller;
    MockHttpServletResponse mockHttpServletResponse;

    @Mock
    CommandGateway gateway;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Asserts.INSTANCE.setAssertsTo(true);
        controller = new ProductRestController();
        mockHttpServletResponse = new MockHttpServletResponse();
    }

    @Test
    public void testAddWithGoodRequestParams() {
        // Arrange
        controller.commandGateway = gateway; //cheating a bit here, but mocking all the axon framework's beans is a pain.
        when(gateway.sendAndWait(any())).thenReturn(null);

        //Act
        controller.add(UUID.randomUUID().toString(), "Test Add Product", mockHttpServletResponse);

        //Assert
        verify(gateway).sendAndWait(any());
        assertTrue(mockHttpServletResponse.getStatus() == HttpServletResponse.SC_CREATED);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFailedAddWithAssertionError() {
        // Arrange
        controller.commandGateway = gateway; //cheating a bit here, but mocking all the axon framework's beans is a pain.
        when(gateway.sendAndWait(any())).thenThrow(AssertionError.class);

        //Act
        controller.add(UUID.randomUUID().toString(), "Test Add Product", mockHttpServletResponse);

        //Assert
        verify(gateway).sendAndWait(any());
        assertTrue(mockHttpServletResponse.getStatus() == HttpServletResponse.SC_BAD_REQUEST);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFailedAddWithCommandExecutionException() {
        // Arrange
        controller.commandGateway = gateway; //cheating a bit here, but mocking all the axon framework's beans is a pain.
        when(gateway.sendAndWait(any())).thenThrow(CommandExecutionException.class);

        //Act
        controller.add(UUID.randomUUID().toString(), "Test Add Product", mockHttpServletResponse);

        //Assert
        verify(gateway).sendAndWait(any());
        assertTrue(mockHttpServletResponse.getStatus() == HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    public void testAddWithBadRequestParams() {

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
