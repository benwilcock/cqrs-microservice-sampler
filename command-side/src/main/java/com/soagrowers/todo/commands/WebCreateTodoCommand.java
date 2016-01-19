package com.soagrowers.todo.commands;

/**
 * Created by ben on 19/01/16.
 */
public class WebCreateTodoCommand {

    private final String id;
    private final String description;

    public WebCreateTodoCommand(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
