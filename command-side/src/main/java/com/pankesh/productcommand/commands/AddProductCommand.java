package com.pankesh.productcommand.commands;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

/**
 * Created by Ben on 07/08/2015.
 */
public class AddProductCommand {

    @TargetAggregateIdentifier
    private final String id;
    private final String name;

    public AddProductCommand(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
