package com.soagrowers.product;

import com.soagrowers.product.aggregates.ProductAggregate;
import com.soagrowers.product.commands.AddProductCommand;
import com.soagrowers.product.commands.MarkProductAsSaleableCommand;
import com.soagrowers.product.commands.MarkProductAsUnsaleableCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by ben on 22/09/15.
 */
public class ProductCommandApiTest {


    @AfterClass
    public static void closeDown() throws InterruptedException{
        // Give the message listeners time to consume from the message queues..
        // Otherwise the messages 'back-up'
        TimeUnit.SECONDS.sleep(5l);
    }

    @Test
    public void testGatewayIsntNull(){
        CommandGateway gateway = ProductCommandApi.getGateway();
        assertNotNull(gateway);
    }

    @Test
    public void testRepositoryIsntNull(){
        EventSourcingRepository<ProductAggregate> repo = ProductCommandApi.getRepository();
        assertNotNull(repo);
    }

    @Test
    public void createLotsOfProducts(){
        for (int i = 0; i < 100; i++) {
            String id = UUID.randomUUID().toString();
            String title = "InitialProduct [" + id + "]";
            ProductCommandApi.getGateway().sendAndWait(new AddProductCommand(id, title));
        }
    }

    @Test
    public void createLotsOfSaleableProducts(){
        for (int i = 0; i < 100; i++) {
            String id = UUID.randomUUID().toString();
            String title = "SaleableProduct [" + id + "]";
            ProductCommandApi.getGateway().sendAndWait(new AddProductCommand(id, title));
            ProductCommandApi.getGateway().sendAndWait(new MarkProductAsSaleableCommand(id));
        }
    }

    @Test
    public void createLotsOfUnsaleableProducts(){
        for (int i = 0; i < 100; i++) {
            String id = UUID.randomUUID().toString();
            String title = "UnsaleableProduct [" + id + "]";
            ProductCommandApi.getGateway().sendAndWait(new AddProductCommand(id, title));
            ProductCommandApi.getGateway().sendAndWait(new MarkProductAsSaleableCommand(id));
            ProductCommandApi.getGateway().sendAndWait(new MarkProductAsUnsaleableCommand(id));
        }
    }
}
