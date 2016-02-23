package com.soagrowers.productview.handlers;

import com.soagrowers.productview.events.ProductAddedEvent;
import com.soagrowers.productview.events.ProductSaleableEvent;
import com.soagrowers.productview.events.ProductUnsaleableEvent;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Handler's (a.k.a. Listeners) can be used to react to events and perform associated
 * actions, such as updating a 'materialised-view' for example.
 * Created by ben on 24/09/15.
 */
@Component
public class EventLoggingHandler {

    private static final Logger LOG = LoggerFactory.getLogger(EventLoggingHandler.class);
    private final String INSTANCE_ID = String.valueOf(Double.valueOf(Math.random() * 100).intValue());

    @EventHandler
    public void handle(ProductAddedEvent event) {
        LOG.debug("INSTANCE-{}: Event({}) [{}] '{}'", INSTANCE_ID, event.getClass().getSimpleName(), event.getId(), event.getName());
    }

    @EventHandler
    public void handle(ProductSaleableEvent event) {
        LOG.debug("INSTANCE-{}: Event({}) [{}]", INSTANCE_ID, event.getClass().getSimpleName(), event.getId());
    }

    @EventHandler
    public void handle(ProductUnsaleableEvent event) {
        LOG.debug("INSTANCE-{}:Event({}) [{}]", INSTANCE_ID, event.getClass().getSimpleName(), event.getId());
    }
}






