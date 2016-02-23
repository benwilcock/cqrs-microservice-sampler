package com.soagrowers.productview;

import com.soagrowers.productview.aggregates.ProductAggregate;
import com.soagrowers.productview.commands.AddProductCommand;
import com.soagrowers.productview.commands.MarkProductAsSaleableCommand;
import com.soagrowers.productview.commands.MarkProductAsUnsaleableCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;

/**
 * Created by ben on 22/09/15.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductCommandTest {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCommandTest.class);
    private static List<String> ids = new ArrayList<String>();
    private static int testVolume = 10;

    @BeforeClass
    public static void setup() {
        for (int i = 0; i < testVolume; i++)
        {
            String id = UUID.randomUUID().toString();
            ids.add(id);
            LOG.info("Created Product Id [{}]", id);
        }
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
    public void testA_createLotsOfProducts() {
        for (String id : ids){
            ProductCommandApi.getGateway().sendAndWait(new AddProductCommand(id, "Product [" + id + "]"));
            LOG.info("Product {} sent command 'Add'.", id);
        }
    }

    @Test
    public void testB_markAllProductsAsSaleable() {
        for (String id : ids) {
            ProductCommandApi.getGateway().sendAndWait(new MarkProductAsSaleableCommand(id));
            LOG.info("Product {} sent command 'Saleable'.", id);
        }

    }

    @Test
    public void testC_markProductsAsUnsaleable() {
        for (String id : ids) {
            ProductCommandApi.getGateway().sendAndWait(new MarkProductAsUnsaleableCommand(id));
            LOG.info("Product {} sent command 'Unsaleable'.", id);
        }
    }

    @AfterClass
    public static void closeDown() throws InterruptedException {
        // Give the message listeners time to consume from the message queues..
        // Otherwise the messages 'back-up'
        TimeUnit.SECONDS.sleep(5l);
    }
}
