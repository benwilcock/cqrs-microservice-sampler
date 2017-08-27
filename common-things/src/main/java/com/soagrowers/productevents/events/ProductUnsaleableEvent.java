package com.soagrowers.productevents.events;

public class ProductUnsaleableEvent extends AbstractEvent {

	private static final long serialVersionUID = 1L;

	public ProductUnsaleableEvent() {
	}

	public ProductUnsaleableEvent(String id) {
		super(id);
	}
}
