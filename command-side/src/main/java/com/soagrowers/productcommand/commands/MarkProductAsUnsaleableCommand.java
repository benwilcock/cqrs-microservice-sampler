package com.soagrowers.productcommand.commands;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

/**
 * Sets the ToDoItem to reopened.
 *
 * @author robertgolder
 */
public class MarkProductAsUnsaleableCommand {

    @TargetAggregateIdentifier
    private final String id;

    /**
     * This constructor must set the Id field, otherwise it's unclear
     * to Axon which aggregate this command is intended for.
     *
     * @param id
     */
    public MarkProductAsUnsaleableCommand(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
