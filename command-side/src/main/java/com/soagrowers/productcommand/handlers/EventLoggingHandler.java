package com.soagrowers.productcommand.handlers;

import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.soagrowers.productevents.events.ProductAddedEvent;
import com.soagrowers.productevents.events.ProductSaleableEvent;
import com.soagrowers.productevents.events.ProductUnsaleableEvent;

/**
 * EventHandler's (a.k.a. EventListeners) are used to react to events and perform associated
 * actions.
 * Created by ben on 24/09/15.
 */
@Component
public class EventLoggingHandler {

    private static final Logger LOG = LoggerFactory.getLogger(EventLoggingHandler.class);
    private static final String IID = String.valueOf(Double.valueOf(Math.random() * 1000).intValue());

    @EventHandler
    public void handle(ProductAddedEvent event) {
        LOG.debug("Instance:{} EventType:{} EventId:[{}] '{}'", IID, event.getClass().getSimpleName(), event.getId(), event.getName());
    }

    @EventHandler
    public void handle(ProductSaleableEvent event) {
        LOG.debug("Instance:{} EventType:{} EventId:[{}]", IID, event.getClass().getSimpleName(), event.getId());
    }

    @EventHandler
    public void handle(ProductUnsaleableEvent event) {
        LOG.debug("Instance:{} EventType:{} EventId:[{}]", IID, event.getClass().getSimpleName(), event.getId());
    }
}






