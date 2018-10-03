package com.pankesh.productcommand.sagas;

import com.pankesh.productevents.events.ProductAddedEvent;
import com.pankesh.productevents.events.ProductDeliverableEvent;
import com.pankesh.productevents.events.ProductSaleableEvent;
import org.axonframework.eventhandling.saga.SagaEventHandler;
import org.axonframework.eventhandling.saga.SagaLifecycle;
import org.axonframework.eventhandling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Saga
public class ProductSaga {

    private static final Logger logger = LoggerFactory.getLogger(ProductSaga.class);

    private String productId;
    private boolean added = false;
    private boolean saleable = false;
    private boolean delivered = false;
    @StartSaga
    @SagaEventHandler(associationProperty = "Id")
    public void handle(ProductAddedEvent event){
        logger.debug("Product is added");
        added=true;
        if((saleable) && (delivered)){
            SagaLifecycle.end();
        }
    }

    @SagaEventHandler(associationProperty = "Id")
    public void handle(ProductSaleableEvent event){
        logger.debug("Product is Saleable");
        saleable = true;
        if((added) && (delivered)){
            SagaLifecycle.end();
        }
    }

    @SagaEventHandler(associationProperty = "Id")
    public void handle(ProductDeliverableEvent event){
        logger.debug("Product is Delivered");
        delivered = true;
        if((saleable) && (added)){
            SagaLifecycle.end();
        }
    }



}
