package com.soagrowers.productview.events;

import java.io.Serializable;

public abstract class AbstractEvent implements Serializable {

    private final String id;

    public AbstractEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
