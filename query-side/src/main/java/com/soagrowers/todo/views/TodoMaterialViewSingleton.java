package com.soagrowers.todo.views;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Ben on 10/08/2015.
 */
public class TodoMaterialViewSingleton {

    private static TodoMaterialViewSingleton ourInstance;
    private static Map<String, String> undone = new HashMap<>();
    private static Map<String, String> done = new HashMap<>();

    public static TodoMaterialViewSingleton getInstance() {
        if(null == ourInstance){
            ourInstance = new TodoMaterialViewSingleton();
        }

        return ourInstance;
    }

    private TodoMaterialViewSingleton() {
    }

    public void addToDo(String id, String desc) {
        undone.put(id, desc);
    }

    public void changeToDone(String id) {
        done.put(id, undone.get(id));
        undone.remove(id);
    }

    public void reopen(String id) {
        undone.put(id, done.get(id));
        done.remove(id);
    }

    public void changeToDone(String id, String completedDate) {
        done.put(id, undone.get(id) + " - completed: " + completedDate + "");
        undone.remove(id);
    }

    public void dumpView() {
        StringBuilder buf = new StringBuilder();

        buf.append("--- Material View ---\n");
        buf.append("ToDo Items: \n");
        for (String id : undone.keySet()) {
            String desc = undone.get(id);
            buf.append("Id: " + id + ", Desc: " + desc + "\n");
        }

        buf.append("Done Items: \n");
        for (String id : done.keySet()) {
            String desc = done.get(id);
            buf.append("Id: " + id + ", Desc: " + desc + "\n");
        }

        buf.append("---------------------");

        System.out.println(buf.toString());
    }

    public void clearView() {
        undone.clear();
        done.clear();
    }

    public int getToDoCount() {
        return undone.size();
    }

    public int getDoneCount() {
        return done.size();
    }

    public String getDoneItemDescription(String toDoId) {
        return done.get(toDoId);
    }
}
