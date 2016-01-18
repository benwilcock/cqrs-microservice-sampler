package com.soagrowers.todo.eventhandlers;

import com.soagrowers.todo.events.TodoCreated;
import com.soagrowers.todo.events.TodoMarkedAsDone;
import com.soagrowers.todo.events.TodoMarkedAsUndone;
import com.soagrowers.todo.views.TodoMaterialViewSingleton;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventhandling.replay.ReplayAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Ben on 10/08/2015.
 */
public class ToDoMaterialViewManager implements ReplayAware {

    private static final Logger LOG = LoggerFactory.getLogger(ToDoMaterialViewManager.class);

    @EventHandler
    public void handle(TodoCreated event) {
        LOG.info("TodoCreated: [{}] '{}'", event.getId(), event.getTitle());
        TodoMaterialViewSingleton.getInstance().addToDo(event.getId(), event.getTitle());
    }

    @EventHandler
    public void handle(TodoMarkedAsDone event) {
        LOG.info("TodoMarkedAsDone: [{}]", event.getId());
        TodoMaterialViewSingleton.getInstance().changeToDone(event.getId());
    }

    @EventHandler
    public void handle(TodoMarkedAsUndone event) {
        LOG.info("TodoMarkedAsUndone: [{}]", event.getId());
        TodoMaterialViewSingleton.getInstance().reopen(event.getId());
    }

    public void beforeReplay() {
        LOG.info("Event Replay is about to START. Clearing the Materialised View...");
        TodoMaterialViewSingleton.getInstance().clearView();
    }

    public void afterReplay() {
        LOG.info("Event Replay has FINISHED.");
    }

    public void onReplayFailed(Throwable cause) {
        LOG.error("Event Replay has FAILED.");
    }
}
