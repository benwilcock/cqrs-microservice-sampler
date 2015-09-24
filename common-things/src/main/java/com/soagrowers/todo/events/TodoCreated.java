package com.soagrowers.todo.events;


public class TodoCreated extends AbstractEvent {


    private final String title;

    public TodoCreated(String todoId, String title) {
        super(todoId);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
