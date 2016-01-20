package com.soagrowers.todo.events;


public class TodoMarkedAsUndone extends AbstractEvent {

    public TodoMarkedAsUndone(String todoId) {
        super(todoId);
    }
}
