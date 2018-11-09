package com.pankesh.productcommand.sagas;

import org.axonframework.eventhandling.saga.EndSaga;
import org.axonframework.eventhandling.saga.SagaEventHandler;
import org.axonframework.eventhandling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pankesh.productevents.events.ProductAddedEvent;
import com.pankesh.productevents.events.ProductDeliveredEvent;


@Saga
public class ProductSaga {

    private static final Logger logger = LoggerFactory.getLogger(ProductSaga.class);

    private boolean added = false;
    private boolean saleable = false;
    private boolean delivered = false;

    @StartSaga
    @SagaEventHandler(associationProperty = "Id")
    public void handle(ProductAddedEvent event){
        logger.debug("Product is added. Saga Started");
        added=true;
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "Id")
    public void handle(ProductDeliveredEvent event){
        logger.debug("Product is Delivered. Saga Ended");
        delivered = true;
    }



}
