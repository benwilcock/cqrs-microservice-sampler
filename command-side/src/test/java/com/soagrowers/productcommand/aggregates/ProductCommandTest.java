package com.soagrowers.productcommand.aggregates;

import com.soagrowers.productcommand.Application;
import com.soagrowers.productcommand.commands.AddProductCommand;
import com.soagrowers.productcommand.commands.MarkProductAsSaleableCommand;
import com.soagrowers.productcommand.commands.MarkProductAsUnsaleableCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;

/**
 * Created by ben on 22/09/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@IntegrationTest({"server.port=0"})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductCommandTest {

    @Autowired
    public CommandGateway gateway;

    @Autowired
    public EventSourcingRepository<ProductAggregate> repo;

    private static final Logger LOG = LoggerFactory.getLogger(ProductCommandTest.class);
    private static List<String> ids = new ArrayList<String>();
    private static int testVolume = 10;

    @BeforeClass
    public static void setup() {
        for (int i = 0; i < testVolume; i++) {
            String id = UUID.randomUUID().toString();
            ids.add(id);
            LOG.info("Created Product Id [{}]", id);
        }
    }

    @Test
    public void testA_GatewayIsntNull() {
        assertNotNull(gateway);
    }

    @Test
    public void testB_RepositoryIsntNull() {
        assertNotNull(repo);
    }


    @Test
    public void testC_createLotsOfProducts() {
        for (String id : ids) {
            gateway.sendAndWait(new AddProductCommand(id, "Product [" + id + "]"));
            LOG.info("Product {} sent command 'Add'.", id);
        }
    }

    @Test
    public void testD_markAllProductsAsSaleable() {
        for (String id : ids) {
            gateway.sendAndWait(new MarkProductAsSaleableCommand(id));
            LOG.info("Product {} sent command 'Saleable'.", id);
        }

    }

    @Test
    public void testE_markProductsAsUnsaleable() {
        for (String id : ids) {
            gateway.sendAndWait(new MarkProductAsUnsaleableCommand(id));
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
