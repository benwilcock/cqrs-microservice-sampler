package com.soagrowers.product;

import com.soagrowers.product.aggregates.ProductAggregate;
import com.soagrowers.product.commands.AddProductCommand;
import com.soagrowers.product.commands.MarkProductAsSaleableCommand;
import com.soagrowers.product.commands.MarkProductAsUnsaleableCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.junit.AfterClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by ben on 22/09/15.
 */
public class ProductCommandApiTest {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCommandApiTest.class);
    private List<String> ids = new ArrayList<String>();


    @AfterClass
    public static void closeDown() throws InterruptedException {
        // Give the message listeners time to consume from the message queues..
        // Otherwise the messages 'back-up'
        TimeUnit.SECONDS.sleep(5l);
    }

    @Test
    public void testGatewayIsntNull() {
        CommandGateway gateway = ProductCommandApi.getGateway();
        assertNotNull(gateway);
    }

    @Test
    public void testRepositoryIsntNull() {
        EventSourcingRepository<ProductAggregate> repo = ProductCommandApi.getRepository();
        assertNotNull(repo);
    }

    @Test
    public void testDuplicateIdempotentAddCommand() {

        try {
            String id = UUID.randomUUID().toString();
            String name = "DuplicateProductTestType1 [" + id + "]";
            AddProductCommand addCommand = new AddProductCommand(id, name);
            ProductCommandApi.getGateway().sendAndWait(addCommand);
            ProductCommandApi.getGateway().sendAndWait(addCommand);
            ProductAggregate agg = ProductCommandApi.loadAggregate(id);
            assertEquals(name, agg.getName());
        } catch (Exception e) {
            LOG.trace("Exception caught", e);
        }

    }

    @Test
    public void testDeliberateDuplicateAddCommand() {

        try {
            String id = UUID.randomUUID().toString();
            String name1 = "DuplicateProductTestType2-i1 [" + id + "]";
            AddProductCommand addCommand1 = new AddProductCommand(id, name1);
            ProductCommandApi.getGateway().sendAndWait(addCommand1);
            ProductAggregate agg1 = ProductCommandApi.loadAggregate(id);
            assertEquals(name1, agg1.getName());

            String name2 = "DuplicateProductTestType2-i2 [" + id + "]";
            AddProductCommand addCommand2 = new AddProductCommand(id, name2);
            ProductCommandApi.getGateway().sendAndWait(addCommand2);
            ProductAggregate agg2 = ProductCommandApi.loadAggregate(id);
            assertEquals(name1, agg2.getName());

        } catch (Exception e) {
            LOG.trace("Exception caught", e);
        }

    }

    @Test
    public void createLotsOfProducts() {
        String title, id;
        for (int i = 0; i < 100; i++) {
            id = UUID.randomUUID().toString();
            title = "Product [" + id + "]";
            ProductCommandApi.getGateway().sendAndWait(new AddProductCommand(id, title));
            ids.add(id);
        }
    }

    @Test
    public void makeProductsAsSaleable() {
        for (String id : ids) {
            ProductCommandApi.getGateway().sendAndWait(new MarkProductAsSaleableCommand(id));
        }

    }

    @Test
    public void markProductsAsUnsaleable() {
        for (String id : ids) {
            ProductCommandApi.getGateway().sendAndWait(new MarkProductAsUnsaleableCommand(id));
        }
    }
}
