package com.soagrowers.todo.events;


public class TodoItemDoneEvent extends AbstractEvent {

    public TodoItemDoneEvent(String todoId) {
        super(todoId);
    }
}
