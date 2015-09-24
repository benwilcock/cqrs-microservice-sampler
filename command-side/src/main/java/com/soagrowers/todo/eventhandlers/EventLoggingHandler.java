package com.soagrowers.todo.eventhandlers;

import com.soagrowers.todo.events.TodoCreated;
import com.soagrowers.todo.events.TodoDone;
import com.soagrowers.todo.events.TodoUndone;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler's (a.k.a. Listeners) can be used to react to events and perform associated
 * actions, such as updating a 'materialised-view' for example.
 * Created by ben on 24/09/15.
 */
public class EventLoggingHandler {

    private  static final Logger LOG = LoggerFactory.getLogger(EventLoggingHandler.class);

    @EventHandler
    public void handle(TodoCreated event) {
        LOG.debug("{}: Event({}) [{}] '{}'", this.getClass().getSimpleName(), event.getClass().getSimpleName(), event.getId(), event.getTitle());
    }

    @EventHandler
    public void handle(TodoDone event) {
        LOG.debug("{}: Event({}) [{}]", this.getClass().getSimpleName(), event.getClass().getSimpleName(), event.getId());
    }

    @EventHandler
    public void handle(TodoUndone event) {
        LOG.debug("{}:Event({}) [{}]", this.getClass().getSimpleName(), event.getClass().getSimpleName(), event.getId());
    }
}






