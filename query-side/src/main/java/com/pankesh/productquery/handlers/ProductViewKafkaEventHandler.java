package com.pankesh.productquery.handlers;


import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ReplayStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pankesh.productevents.events.ProductAddedEvent;
import com.pankesh.productevents.events.ProductSaleableEvent;
import com.pankesh.productevents.events.ProductUnsaleableEvent;
import com.pankesh.productquery.domain.Product;
import com.pankesh.productquery.repository.ProductRepository;

@Component
@ProcessingGroup("productQueryKafka")
public class ProductViewKafkaEventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ProductViewKafkaEventHandler.class);

    @Autowired
    private ProductRepository productRepository;


    @EventHandler
    public void handle(ProductAddedEvent event, ReplayStatus replayStatus) {
        LOG.info("ProductAddedEvent In Query via Kafka: [{}] '{}'", event.getId(), event.getName());
        productRepository.save(new Product(event.getId(), event.getName(), false));
        LOG.info("After save in Query side");
    }

    @EventHandler
    public void handle(ProductSaleableEvent event) {
        LOG.info("ProductSaleableEvent via Kafka: [{}]", event.getId());
        if (productRepository.exists(event.getId())) {
            Product product = productRepository.findOne(event.getId());
            if (!product.isSaleable()) {
                product.setSaleable(true);
                productRepository.save(product);
            }
        }
    }

    @EventHandler
    public void handle(ProductUnsaleableEvent event) {
        LOG.info("ProductUnsaleableEvent via Kafka: [{}]", event.getId());

        if (productRepository.exists(event.getId())) {
            Product product = productRepository.findOne(event.getId());
            if (product.isSaleable()) {
                product.setSaleable(false);
                productRepository.save(product);
            }
        }
    }
}
