package com.soagrowers.todo.aggregates;


import com.soagrowers.todo.commands.*;
import com.soagrowers.todo.events.*;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Ben on 07/08/2015.
 */
public class TodoTest {

    private FixtureConfiguration fixture;

    @Before
    public void setUp() throws Exception {
        fixture = Fixtures.newGivenWhenThenFixture(Todo.class);
    }

    @Test
    public void testCreateToDoItem() throws Exception {
        fixture.given()
                .when(new CreateTodoCommand("todo1", "need to implement the aggregate"))
                .expectEvents(new TodoCreated("todo1", "need to implement the aggregate"));
    }

    @Test
    public void testMarkToDoItemAsCompleted() throws Exception {
        fixture.given(new TodoCreated("todo1", "need to implement the aggregate"))
                .when(new MarkTodoAsDoneCommand("todo1"))
                .expectVoidReturnType()
                .expectEvents(new TodoMarkedAsDone("todo1"));
    }
}
