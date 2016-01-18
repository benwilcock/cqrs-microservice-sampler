package com.soagrowers.todo.eventhandlers;

import com.soagrowers.todo.events.TodoCreated;
import com.soagrowers.todo.events.TodoMarkedAsDone;
import com.soagrowers.todo.events.TodoMarkedAsUndone;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler's (a.k.a. Listeners) can be used to react to events and perform associated
 * actions, such as updating a 'materialised-view' for example.
 * Created by ben on 24/09/15.
 */
public class EventLoggingHandler {

    private static final Logger LOG = LoggerFactory.getLogger(EventLoggingHandler.class);
    private final String INSTANCE_ID = String.valueOf(Double.valueOf(Math.random() * 100).intValue());

    @EventHandler
    public void handle(TodoCreated event) {
        LOG.debug("INSTANCE-{}: Event({}) [{}] '{}'", INSTANCE_ID, event.getClass().getSimpleName(), event.getId(), event.getTitle());
    }

    @EventHandler
    public void handle(TodoMarkedAsDone event) {
        LOG.debug("INSTANCE-{}: Event({}) [{}]", INSTANCE_ID, event.getClass().getSimpleName(), event.getId());
    }

    @EventHandler
    public void handle(TodoMarkedAsUndone event) {
        LOG.debug("INSTANCE-{}:Event({}) [{}]", INSTANCE_ID, event.getClass().getSimpleName(), event.getId());
    }
}






