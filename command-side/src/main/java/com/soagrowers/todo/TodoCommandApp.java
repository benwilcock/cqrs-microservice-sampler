package com.soagrowers.todo;


/*
import com.soagrowers.todo.aggregates.Todo;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.unitofwork.DefaultUnitOfWorkFactory;
import org.axonframework.unitofwork.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
*/

/**
 * Created by ben on 21/09/15.
 */
public class TodoCommandApp {
/*

    private final static Logger LOG = LoggerFactory.getLogger(TodoCommandApp.class);
    private final static CommandGateway commandGateway;
    private final static ApplicationContext applicationContext;
    private final static EventSourcingRepository<Todo> repository;

    static {
        applicationContext = new ClassPathXmlApplicationContext("commandContext.xml");
        commandGateway = applicationContext.getBean(CommandGateway.class);
        repository = (EventSourcingRepository<Todo>) applicationContext.getBean("todoRepository");
    }

    private TodoCommandApp() {
    }

    public static CommandGateway getGateway() {
        return commandGateway;
    }

    public static EventSourcingRepository<Todo> getRepository() {
        return repository;
    }

    public static final Todo loadAggregate(String toDoItemId) {
        LOG.debug("Loading: 'Todo' {}", toDoItemId);
        UnitOfWork unitOfWork = new DefaultUnitOfWorkFactory().createUnitOfWork();
        Todo item = repository.load(toDoItemId);
        unitOfWork.commit();
        LOG.debug("Loaded: 'Todo' {}, '{}', complete={}", item.getId(), item.getTitle(), item.isDone());
        return item;
    }
*/


}
