package com.pankesh.productquery.handlers;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pankesh.productevents.events.ProductAddedEvent;
import com.pankesh.productevents.events.ProductDeliveredEvent;
import com.pankesh.productevents.events.ProductSaleableEvent;
import com.pankesh.productevents.events.ProductUnsaleableEvent;
import com.pankesh.productquery.domain.Product;
import com.pankesh.productquery.repository.ProductRepository;

@Component
@ProcessingGroup("productQuery")
public class ProductViewEventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ProductViewEventHandler.class);

    @Autowired
    private ProductRepository productRepository;

    @EventHandler
    public void handle(ProductAddedEvent event) {
        LOG.info("ProductAddedEvent: [{}] '{}'", event.getId(), event.getName());
        productRepository.save(new Product(event.getId(), event.getName(), false));
    }

    @EventHandler
    public void handle(ProductSaleableEvent event) {
        LOG.info("ProductSaleableEvent: [{}]", event.getId());
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
        LOG.info("ProductUnsaleableEvent: [{}]", event.getId());

        if (productRepository.exists(event.getId())) {
            Product product = productRepository.findOne(event.getId());
            if (product.isSaleable()) {
                product.setSaleable(false);
                productRepository.save(product);
            }
        }
    }

    @EventHandler
    public void handle(ProductDeliveredEvent event) {
        LOG.info("ProductDeliveredEvent: [{}]", event.getId());

        if (productRepository.exists(event.getId())) {
            Product product = productRepository.findOne(event.getId());
            if (!product.isDelivered()) {
                product.setDelivered(true);
                productRepository.save(product);
            }
        }
    }
}
