package com.pankesh.productquery;

import javax.servlet.http.HttpServletResponse;

import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.axonframework.kafka.eventhandling.consumer.KafkaTrackingToken;
import org.axonframework.messaging.StreamableMessageSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReplayRestController {

    private static final Logger LOG = LoggerFactory.getLogger(ReplayRestController.class);

    @Autowired
    EventProcessingConfiguration eventProcessingConfiguration;

    @RequestMapping(value = "/replay/{group}", method = RequestMethod.POST)
    public void replay(@PathVariable(value = "group") String group, HttpServletResponse response) {

        LOG.debug("Start replaying...");
//        final Supplier<? extends RuntimeException> notFoundSupplier = () -> new IllegalArgumentException(
//                "Processor " + group + " not registered.");
//        this.eventProcessingConfiguration.eventProcessor(group).orElseThrow(notFoundSupplier).shutDown();
//        this.eventProcessingConfiguration.eventProcessor(group).orElseThrow(notFoundSupplier).start();
        this.eventProcessingConfiguration.eventProcessorByProcessingGroup(group, TrackingEventProcessor.class)
        .ifPresent(trackingEventProcessor -> {
            trackingEventProcessor.shutDown();
            trackingEventProcessor.resetTokens();
            trackingEventProcessor.start();
        });
    }
}
