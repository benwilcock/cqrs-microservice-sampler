package com.soagrowers.productcommand.aggregates;

import com.soagrowers.productcommand.Application;
import com.soagrowers.productcommand.commands.AddProductCommand;
import com.soagrowers.productcommand.commands.MarkProductAsSaleableCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.EventSourcingRepository;
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

import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by ben on 23/02/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@IntegrationTest({"server.port=0"})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductCommandDuplicatesTest {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCommandDuplicatesTest.class);

    @Autowired
    public CommandGateway gateway;

    @Autowired
    public EventSourcingRepository<ProductAggregate> repo;

    @Test
    public void testA_GatewayIsntNull() {
        assertNotNull(gateway);
    }

    @Test
    public void testB_RepositoryIsntNull() {
        assertNotNull(repo);
    }

    @Test
    public void testDuplicateIdempotentAddCommand() {

        try {
            String id = UUID.randomUUID().toString();
            String name = "DuplicateProductTestType1 [" + id + "]";
            AddProductCommand addCommand = new AddProductCommand(id, name);
            gateway.sendAndWait(addCommand);
            gateway.sendAndWait(addCommand);
            ProductAggregate agg = repo.load(id);
            assertEquals(name, agg.getName());
        } catch (Exception e) {
            LOG.trace("Exception caught", e);
        }

    }

    @Test
    public void testDeliberateDuplicateAddCommand() {

        ProductAggregate storedAggregate;
        String id = UUID.randomUUID().toString();

        try {
            // Add a product with a specific ID
            String name1 = "DuplicateProductTestType2-i1 [" + id + "]";
            AddProductCommand addCommand1 = new AddProductCommand(id, name1);
            gateway.sendAndWait(addCommand1);
            storedAggregate = repo.load(id);
            assertEquals(name1, storedAggregate.getName());

            // Mark that product as saleable
            MarkProductAsSaleableCommand saleableCommand = new MarkProductAsSaleableCommand(id);
            gateway.sendAndWait(saleableCommand);
            storedAggregate = repo.load(id);
            assertTrue(storedAggregate.isSaleable());

            // Now try and create a product that shares the same ID (BAD)
            String name2 = "DuplicateProductTestType2-i2 [" + id + "]";
            AddProductCommand addCommand2 = new AddProductCommand(id, name2);
            gateway.sendAndWait(addCommand2);
            storedAggregate = repo.load(id);
            assertEquals(name1, storedAggregate.getName());
            assertTrue(storedAggregate.isSaleable());
            assertNotEquals(name2, storedAggregate.getName());

        } catch (Exception e) {
            LOG.trace("Exception caught", e);
        }

    }
}
