package com.soagrowers.productview;

import com.soagrowers.productview.aggregates.ProductAggregate;
import com.soagrowers.productview.commands.AddProductCommand;
import com.soagrowers.productview.commands.MarkProductAsSaleableCommand;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by ben on 23/02/16.
 */
public class ProductCommandDuplicatesTest {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCommandDuplicatesTest.class);

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

        ProductAggregate storedAggregate;
        String id = UUID.randomUUID().toString();

        try {
            // Add a product with a specific ID
            String name1 = "DuplicateProductTestType2-i1 [" + id + "]";
            AddProductCommand addCommand1 = new AddProductCommand(id, name1);
            ProductCommandApi.getGateway().sendAndWait(addCommand1);
            storedAggregate = ProductCommandApi.loadAggregate(id);
            assertEquals(name1, storedAggregate.getName());

            // Mark that product as saleable
            MarkProductAsSaleableCommand saleableCommand = new MarkProductAsSaleableCommand(id);
            ProductCommandApi.getGateway().sendAndWait(saleableCommand);
            storedAggregate = ProductCommandApi.loadAggregate(id);
            assertTrue(storedAggregate.isSaleable());

            // Now try and create a product that shares the same ID (BAD)
            String name2 = "DuplicateProductTestType2-i2 [" + id + "]";
            AddProductCommand addCommand2 = new AddProductCommand(id, name2);
            ProductCommandApi.getGateway().sendAndWait(addCommand2);
            storedAggregate = ProductCommandApi.loadAggregate(id);
            assertEquals(name1, storedAggregate.getName());
            assertTrue(storedAggregate.isSaleable());
            assertNotEquals(name2, storedAggregate.getName());

        } catch (Exception e) {
            LOG.trace("Exception caught", e);
        }

    }
}
