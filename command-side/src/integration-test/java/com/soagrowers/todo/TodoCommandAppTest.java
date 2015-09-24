package com.soagrowers.todo;

import com.soagrowers.todo.aggregates.Todo;
import com.soagrowers.todo.commands.CreateCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by ben on 22/09/15.
 */
public class TodoCommandAppTest {


    @AfterClass
    public static void closeDown() throws InterruptedException{
        // Give the message listeners time to consume from the message queues..
        // Otherwise the messages 'back-up'
        TimeUnit.SECONDS.sleep(3l);
    }

    @Test
    public void testGatewayIsntNull(){
        CommandGateway gateway = TodoCommandApp.getGateway();
        assertNotNull(gateway);
    }

    @Test
    public void testRepositoryIsntNull(){
        EventSourcingRepository<Todo> repo = TodoCommandApp.getRepository();
        assertNotNull(repo);
    }

    @Test
    public void testCommandDeliveryAndEventSourcingWorks()  {
        String id = UUID.randomUUID().toString();
        String title = "Test command delivery";
        TodoCommandApp.getGateway().send(new CreateCommand(id, title));
        Todo todo = TodoCommandApp.loadAggregate(id);
        assertEquals(id, todo.getId());
        assertEquals(title, todo.getTitle());
        assertFalse(todo.isDone());
    }
}
