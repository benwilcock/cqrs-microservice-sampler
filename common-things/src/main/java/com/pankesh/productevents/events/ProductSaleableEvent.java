package com.pankesh.productevents.events;


public class ProductSaleableEvent extends AbstractEvent {

    private static final long serialVersionUID = 566047158038996746L;

    public ProductSaleableEvent() {
    }

    public ProductSaleableEvent(String id) {
        super(id);
    }
}
