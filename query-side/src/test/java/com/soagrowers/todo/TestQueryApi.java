package com.soagrowers.todo;

import org.junit.Test;

/**
 * Created by ben on 25/09/15.
 */
public class TestQueryApi {

    @Test
    public void testQueryApiBoots(){
        TodoQueryApi.getInstance().getAllTodos();
    }
}
