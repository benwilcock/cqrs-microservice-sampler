package com.soagrowers.todo.data;

/**
 * Created by ben on 25/09/15.
 */
public class TodoDataItem {

    private final String id;
    private final String title;
    private final boolean done;

    public TodoDataItem(String id, String title, boolean done) {
        this.id = id;
        this.title = title;
        this.done = done;
    }

    public TodoDataItem(String id, String title) {
        this.id = id;
        this.title = title;
        this.done = false;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isDone() {
        return done;
    }
}
