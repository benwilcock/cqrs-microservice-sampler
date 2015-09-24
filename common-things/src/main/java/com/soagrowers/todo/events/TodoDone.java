package com.soagrowers.todo.events;


public class TodoDone extends AbstractEvent {

    public TodoDone(String todoId) {
        super(todoId);
    }
}
