package com.pankesh.productcommand.handlers;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.pankesh.productevents.events.ProductAddedEvent;
import com.pankesh.productevents.events.ProductSaleableEvent;
import com.pankesh.productevents.events.ProductUnsaleableEvent;

@Component
@ProcessingGroup("productQueryTracking")
public class ProductViewTrackingEventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ProductViewTrackingEventHandler.class);

    @EventHandler
    public void handle(ProductAddedEvent event) {
        LOG.info("Tracking ProductAddedEvent: [{}] '{}'", event.getId(), event.getName());
    }

    @EventHandler
    public void handle(ProductSaleableEvent event) {
        LOG.info("Tracking ProductSaleableEvent: [{}]", event.getId());
    }

    @EventHandler
    public void handle(ProductUnsaleableEvent event) {
        LOG.info("Tracking ProductUnsaleableEvent: [{}]", event.getId());
    }
}
