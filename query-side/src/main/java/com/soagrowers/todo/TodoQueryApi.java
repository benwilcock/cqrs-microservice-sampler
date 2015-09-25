package com.soagrowers.todo;


import com.soagrowers.todo.data.TodoDataItem;
import com.soagrowers.todo.views.TodoMaterialViewSingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ben on 21/09/15.
 */
public class TodoQueryApi {


    private final static Logger LOG = LoggerFactory.getLogger(TodoQueryApi.class);
    private final static ApplicationContext applicationContext;
    private final static String CONTEXT_FILE_NAME = "queryContext.xml";
    private final static TodoMaterialViewSingleton view = TodoMaterialViewSingleton.getInstance();
    private static TodoQueryApi instance;

    static {
        LOG.info("Starting the TodoQueryApi with context file: {}", CONTEXT_FILE_NAME);
        applicationContext = new ClassPathXmlApplicationContext(CONTEXT_FILE_NAME);
    }

    private TodoQueryApi() {
    }

    public int getTodoCount(){
        return view.getToDoCount();
    }

    public int getDoneCount(){
        return view.getDoneCount();
    }

    public  List<TodoDataItem> getOutstandingTodos(){
        return new ArrayList<TodoDataItem>();
    }

    public  List<TodoDataItem> getAllTodos(){
        return new ArrayList<TodoDataItem>();
    }

    public  List<TodoDataItem> getDoneTodos(){
        return new ArrayList<TodoDataItem>();
    }

    public  List<TodoDataItem> getUnoneTodos(){
        return new ArrayList<TodoDataItem>();
    }

    public static TodoQueryApi getInstance(){
        if(null == instance){
            instance = new TodoQueryApi();
        }

        return instance;
    }

}
