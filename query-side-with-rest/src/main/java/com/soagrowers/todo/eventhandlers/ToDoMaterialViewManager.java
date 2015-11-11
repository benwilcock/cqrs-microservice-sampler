package com.soagrowers.todo.eventhandlers;

import com.soagrowers.todo.TodoMaterialViewApplication;
import com.soagrowers.todo.data.Todo;
import com.soagrowers.todo.data.TodoRepository;
import com.soagrowers.todo.events.TodoCreated;
import com.soagrowers.todo.events.TodoDone;
import com.soagrowers.todo.events.TodoUndone;
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
        TodoRepository repo = TodoMaterialViewApplication.getBootApplicationContext().getBean(TodoRepository.class);
        repo.save(new Todo(event.getId(), event.getTitle(), false));
    }

    @EventHandler
    public void handle(TodoDone event) {
        LOG.info("TodoDone: [{}]", event.getId());
        TodoRepository repo = TodoMaterialViewApplication.getBootApplicationContext().getBean(TodoRepository.class);

        if(repo.exists(event.getId())){
            Todo todo = repo.findOne(event.getId());
            if(!todo.isStatus()) {
                todo.setStatus(true);
                repo.save(todo);
            }
        }
    }

    @EventHandler
    public void handle(TodoUndone event) {
        LOG.info("TodoUndone: [{}]", event.getId());
        TodoRepository repo = TodoMaterialViewApplication.getBootApplicationContext().getBean(TodoRepository.class);

        if(repo.exists(event.getId())){
            Todo todo = repo.findOne(event.getId());
            if(todo.isStatus()) {
                todo.setStatus(false);
                repo.save(todo);
            }
        }
    }

    public void beforeReplay() {
        LOG.info("Event Replay is about to START. Clearing the Materialised View...");
    }

    public void afterReplay() {
        LOG.info("Event Replay has FINISHED.");
    }

    public void onReplayFailed(Throwable cause) {
        LOG.error("Event Replay has FAILED.");
    }
}
