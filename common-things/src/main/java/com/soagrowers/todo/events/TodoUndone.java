package com.soagrowers.todo.events;


public class TodoUndone extends AbstractEvent {

    public TodoUndone(String todoId) {
        super(todoId);
    }
}
