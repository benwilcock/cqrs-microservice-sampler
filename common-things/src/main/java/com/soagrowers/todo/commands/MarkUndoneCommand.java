package com.soagrowers.todo.commands;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

/**
 * Sets the ToDoItem to reopened.
 *
 * @author robertgolder
 */
public class MarkUndoneCommand {

    @TargetAggregateIdentifier
    private final String todoId;

    /**
     * This constructor must set the Id field, otherwise it's unclear
     * to Axon which aggregate this command is intended for.
     *
     * @param todoId
     */
    public MarkUndoneCommand(String todoId) {
        this.todoId = todoId;
    }

    public String getTodoId() {
        return todoId;
    }
}
