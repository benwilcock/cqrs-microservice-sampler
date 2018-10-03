package com.pankesh.productevents.events;


public class ProductDeliverableEvent extends AbstractEvent {

    private static final long serialVersionUID = 566047158038996746L;

    public ProductDeliverableEvent() {
    }

    public ProductDeliverableEvent(String id) {
        super(id);
    }
}
