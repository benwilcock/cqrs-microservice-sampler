package com.soagrowers.todo.events;


public class ToDoItemDoneEvent extends AbstractEvent {

    public ToDoItemDoneEvent(String todoId) {
        super(todoId);
    }
}
