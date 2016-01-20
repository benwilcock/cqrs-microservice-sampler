package com.soagrowers.todo.events;


public class TodoMarkedAsDone extends AbstractEvent {

    public TodoMarkedAsDone(String todoId) {
        super(todoId);
    }
}
