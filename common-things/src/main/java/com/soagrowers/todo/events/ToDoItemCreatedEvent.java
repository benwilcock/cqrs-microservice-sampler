package com.soagrowers.todo.events;


public class ToDoItemCreatedEvent extends AbstractEvent {


    private final String description;

    public ToDoItemCreatedEvent(String todoId, String description) {
        super(todoId);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
