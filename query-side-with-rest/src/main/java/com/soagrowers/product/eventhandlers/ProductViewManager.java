package com.soagrowers.product.eventhandlers;

import com.soagrowers.product.ProductViewApplication;
import com.soagrowers.product.data.Product;
import com.soagrowers.product.data.ProductRepository;
import com.soagrowers.product.events.*;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventhandling.replay.ReplayAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Ben on 10/08/2015.
 */
public class ProductViewManager implements ReplayAware {

    private static final Logger LOG = LoggerFactory.getLogger(ProductViewManager.class);

    @EventHandler
    public void handle(ProductAddedEvent event) {
        LOG.info("ProductAddedEvent: [{}] '{}'", event.getId(), event.getName());
        ProductRepository repo = ProductViewApplication.getBootApplicationContext().getBean(ProductRepository.class);
        repo.save(new Product(event.getId(), event.getName(), false));
    }

    @EventHandler
    public void handle(ProductSaleableEvent event) {
        LOG.info("ProductSaleableEvent: [{}]", event.getId());
        ProductRepository repo = ProductViewApplication.getBootApplicationContext().getBean(ProductRepository.class);

        if(repo.exists(event.getId())){
            Product product = repo.findOne(event.getId());
            if(!product.isSaleable()) {
                product.setSaleable(true);
                repo.save(product);
            }
        }
    }

    @EventHandler
    public void handle(ProductUnsaleableEvent event) {
        LOG.info("ProductUnsaleableEvent: [{}]", event.getId());
        ProductRepository repo = ProductViewApplication.getBootApplicationContext().getBean(ProductRepository.class);

        if(repo.exists(event.getId())){
            Product product = repo.findOne(event.getId());
            if(product.isSaleable()) {
                product.setSaleable(false);
                repo.save(product);
            }
        }
    }

    public void beforeReplay() {
        LOG.info("Event Replay is about to START. Clearing the View...");
    }

    public void afterReplay() {
        LOG.info("Event Replay has FINISHED.");
    }

    public void onReplayFailed(Throwable cause) {
        LOG.error("Event Replay has FAILED.");
    }
}
