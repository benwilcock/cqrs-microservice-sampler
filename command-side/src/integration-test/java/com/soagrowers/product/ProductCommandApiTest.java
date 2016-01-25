package com.soagrowers.product;

import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoException;
import com.soagrowers.product.aggregates.ProductAggregate;
import com.soagrowers.product.commands.AddProductCommand;
import com.soagrowers.product.commands.MarkProductAsSaleableCommand;
import com.soagrowers.product.commands.MarkProductAsUnsaleableCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.repository.ConcurrencyException;
import org.junit.AfterClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by ben on 22/09/15.
 */
public class ProductCommandApiTest {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCommandApiTest.class);


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
    public void testDuplicateAddCommandType1(){

        try {
            String id = UUID.randomUUID().toString();
            String name = "DuplicateProductTestType1 [" + id + "]";
            AddProductCommand addCommand = new AddProductCommand(id, name);
            ProductCommandApi.getGateway().sendAndWait(addCommand);
            ProductCommandApi.getGateway().sendAndWait(addCommand);
            fail();
        } catch (MongoException.DuplicateKey dke){
            assertTrue(true);
        } catch (ConcurrencyException ce){
            //This is what we're expecting
            assertTrue(true);
        }

    }

    @Test
    public void testDuplicateAddCommandType2(){

        try {
            String id = UUID.randomUUID().toString();
            String name1 = "DuplicateProductTestType2-1 [" + id + "]";
            AddProductCommand addCommand1 = new AddProductCommand(id, name1);
            ProductCommandApi.getGateway().sendAndWait(addCommand1);
            ProductAggregate agg1 = ProductCommandApi.loadAggregate(id);
            assertEquals(name1, agg1.getName());

            String name2 = "DuplicateProductTestType2-2 [" + id + "]";
            AddProductCommand addCommand2 = new AddProductCommand(id, name2);
            ProductCommandApi.getGateway().sendAndWait(addCommand2);
            ProductAggregate agg2 = ProductCommandApi.loadAggregate(id);
            assertEquals(name2, agg2.getName());

        } catch (MongoException.DuplicateKey dke){
            assertTrue(true);
        } catch (ConcurrencyException ce){
            //This is what we're expecting
            assertTrue(true);
        }

    }

    //@Test
    public void createLotsOfProducts(){
        for (int i = 0; i < 100; i++) {
            String id = UUID.randomUUID().toString();
            String title = "InitialProduct [" + id + "]";
            ProductCommandApi.getGateway().sendAndWait(new AddProductCommand(id, title));
        }
    }

    //@Test
    public void createLotsOfSaleableProducts(){
        for (int i = 0; i < 100; i++) {
            String id = UUID.randomUUID().toString();
            String title = "SaleableProduct [" + id + "]";
            ProductCommandApi.getGateway().sendAndWait(new AddProductCommand(id, title));
            ProductCommandApi.getGateway().sendAndWait(new MarkProductAsSaleableCommand(id));
        }
    }

    //@Test
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
