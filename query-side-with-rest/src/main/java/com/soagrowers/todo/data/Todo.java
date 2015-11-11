package com.soagrowers.todo.data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by ben on 07/10/15.
 */

@Entity
public class Todo {

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO) - Left off as this demo is read-only
    private String id;

    private String title;
    private boolean status;

    public Todo() {
    }

    public Todo(String id, String title, boolean status) {
        this.id = id;
        this.title = title;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
