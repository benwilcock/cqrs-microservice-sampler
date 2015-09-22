package com.soagrowers.todo.events;


public class TodoItemCreatedEvent extends AbstractEvent {


    private final String description;

    public TodoItemCreatedEvent(String todoId, String description) {
        super(todoId);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
