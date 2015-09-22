package com.soagrowers.todo.events;


public class ToDoItemUndoneEvent extends AbstractEvent {

    public ToDoItemUndoneEvent(String todoId) {
        super(todoId);
    }
}
