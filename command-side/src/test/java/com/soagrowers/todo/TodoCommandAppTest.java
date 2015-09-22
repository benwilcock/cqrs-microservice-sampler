package com.soagrowers.todo;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by ben on 22/09/15.
 */
public class TodoCommandAppTest {


    @Test
    public void testCommandApp(){
        CommandGateway gateway = TodoCommandApp.getGateway();
        assertNotNull(gateway);
    }
}
