package com.soagrowers.product.aggregates;

import com.soagrowers.product.commands.AddProductCommand;
import com.soagrowers.product.commands.MarkProductAsSaleableCommand;
import com.soagrowers.product.events.ProductAddedEvent;
import com.soagrowers.product.events.ProductSaleableEvent;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Ben on 07/08/2015.
 */
public class ProductAggregateTest {

    private FixtureConfiguration fixture;

    @Before
    public void setUp() throws Exception {
        fixture = Fixtures.newGivenWhenThenFixture(ProductAggregate.class);
    }

    @Test
    public void testAddProduct() throws Exception {
        fixture.given()
                .when(new AddProductCommand("product1", "need to implement the aggregate"))
                .expectEvents(new ProductAddedEvent("product1", "need to implement the aggregate"));
    }

    @Test
    public void testMarkProductItemAsSaleable() throws Exception {
        fixture.given(new ProductAddedEvent("product1", "need to implement the aggregate"))
                .when(new MarkProductAsSaleableCommand("product1"))
                .expectVoidReturnType()
                .expectEvents(new ProductSaleableEvent("product1"));
    }
}
