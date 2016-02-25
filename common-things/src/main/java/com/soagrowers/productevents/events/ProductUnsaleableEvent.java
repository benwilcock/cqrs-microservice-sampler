package com.soagrowers.productevents.events;


public class ProductUnsaleableEvent extends AbstractEvent {

    public ProductUnsaleableEvent() {
    }

    public ProductUnsaleableEvent(String id) {
        super(id);
    }
}
