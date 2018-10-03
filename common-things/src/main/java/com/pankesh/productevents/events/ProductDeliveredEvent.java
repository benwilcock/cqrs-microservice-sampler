package com.pankesh.productevents.events;


public class ProductDeliveredEvent extends AbstractEvent {

    private static final long serialVersionUID = 566047158038996746L;

    public ProductDeliveredEvent() {
    }

    public ProductDeliveredEvent(String id) {
        super(id);
    }
}
