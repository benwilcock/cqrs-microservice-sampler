package com.pankesh.productcommand.commands;

import org.axonframework.commandhandling.TargetAggregateIdentifier;


public class MarkProductAsDeliverableCommand {

    /**
     * How does Axon know which Aggregate to Mark as completed? It uses
     * the TargetAggregateIdentifier annotation so that it can get the
     * correct one based on the Id in the annotated field.
     */
    @TargetAggregateIdentifier
    private final String id;

    /**
     * This constructor must set the Id field, otherwise it's unclear
     * to Axon which aggregate this command is intended for.
     *
     * @param id
     */
    public MarkProductAsDeliverableCommand(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
