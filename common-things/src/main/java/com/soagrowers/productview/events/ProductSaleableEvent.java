package com.soagrowers.productview.events;


public class ProductSaleableEvent extends AbstractEvent {

    public ProductSaleableEvent() {
    }

    public ProductSaleableEvent(String id) {
        super(id);
    }
}
