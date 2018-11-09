package com.pankesh.productquery.handlers;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.pankesh.productevents.events.ProductAddedEvent;
import com.pankesh.productevents.events.ProductDeliveredEvent;
import com.pankesh.productevents.events.ProductSaleableEvent;
import com.pankesh.productevents.events.ProductUnsaleableEvent;


@Component
@ProcessingGroup("productQuery")
public class EventLoggingHandler {

    private static final Logger LOG = LoggerFactory.getLogger(EventLoggingHandler.class);

    @Value("${spring.application.index}")
    private Integer indexId;

    @EventHandler
    public void handle(ProductAddedEvent event) {
        LOG.debug("Instance:[{}] Event:{} Id:{} Name:'{}'", indexId, event.getClass().getSimpleName(), event.getId(), event.getName());
    }

    @EventHandler
    public void handle(ProductSaleableEvent event) {
        LOG.debug("Instance:[{}] Event:{} Id:{}", indexId, event.getClass().getSimpleName(), event.getId());
    }

    @EventHandler
    public void handle(ProductUnsaleableEvent event) {
        LOG.debug("Instance:[{}] Event:{} Id:{}", indexId, event.getClass().getSimpleName(), event.getId());
    }
    
    @EventHandler
    public void handle(ProductDeliveredEvent event) {
        LOG.debug("Instance:[{}] Event:{} Id:{}", indexId, event.getClass().getSimpleName(), event.getId());
    }
}






