package com.soagrowers.todo.events;


public class TodoItemUndoneEvent extends AbstractEvent {

    public TodoItemUndoneEvent(String todoId) {
        super(todoId);
    }
}
