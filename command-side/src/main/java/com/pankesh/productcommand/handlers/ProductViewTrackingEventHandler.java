package com.pankesh.productcommand.handlers;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventhandling.GenericEventMessage;
import org.axonframework.eventhandling.ReplayStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pankesh.productevents.events.ProductAddedEvent;
import com.pankesh.productevents.events.ProductSaleableEvent;
import com.pankesh.productevents.events.ProductUnsaleableEvent;

@Component
@ProcessingGroup("productQueryTracking")
public class ProductViewTrackingEventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ProductViewTrackingEventHandler.class);

    @Autowired
    EventBus eventBus;

    
    @EventHandler
    public void handle(ProductAddedEvent event, ReplayStatus replayStatus) {
        LOG.info("Tracking ProductAddedEvent: [{}] '{}'. Replay status: {}.", event.getId(), event.getName(), replayStatus.isReplay());
        
        if (replayStatus.isReplay()) {
            EventMessage<ProductAddedEvent> asEventMessage = GenericEventMessage.<ProductAddedEvent>asEventMessage(event);
            eventBus.publish(asEventMessage);
        }
        
    }

    @EventHandler
    public void handle(ProductSaleableEvent event, ReplayStatus replayStatus) {
        LOG.info("Tracking ProductSaleableEvent: [{}] Replay status: {}", event.getId(), replayStatus.isReplay());

        if (replayStatus.isReplay()) {
            EventMessage<ProductSaleableEvent> asEventMessage = GenericEventMessage.<ProductSaleableEvent>asEventMessage(event);
            eventBus.publish(asEventMessage);
        }
        
}

    @EventHandler
    public void handle(ProductUnsaleableEvent event, ReplayStatus replayStatus) {
        LOG.info("Tracking ProductUnsaleableEvent: [{}] Replay status: {}", event.getId(), replayStatus.isReplay());

        if (replayStatus.isReplay()) {
            EventMessage<ProductUnsaleableEvent> asEventMessage = GenericEventMessage.<ProductUnsaleableEvent>asEventMessage(event);
            eventBus.publish(asEventMessage);
        }
        
}

    @EventHandler
    public void sendAddProductEmail(ProductAddedEvent event, ReplayStatus replayStatus) {
        LOG.info("Tracking ProductAddedEvent to send Email: [{}] '{}'. Replay status: {}.", event.getId(), event.getName(), replayStatus.isReplay());
    }

}
