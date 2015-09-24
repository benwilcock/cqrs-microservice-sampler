package com.soagrowers.todo.aggregates;

import com.soagrowers.todo.commands.*;
import com.soagrowers.todo.events.*;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.axonframework.eventsourcing.annotation.EventSourcingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Todo is essentially a DDD AggregateRoot (from the DDD concept). In event-sourced
 * systems, Aggregates are often stored and retreived using a 'Repository'. In the
 * simplest terms, Aggregates are the sum of their applied 'Events'.
 * <p>
 * The Repository stores the aggregate's Events in an 'Event Store'. When an Aggregate
 * is re-loaded by the repository, the Repository re-applies all the stored events
 * to the aggregate thereby re-creating the logical state of the Aggregate.
 * <p>
 * The Todo Aggregate can handle and react to 'Commands', and when it reacts
 * to these commands it creates and 'applies' Events that represent the logical changes
 * to be made. These Events are also handled by the Todo.
 * <p>
 * Axon takes care of much of this via the CommandBus, EventBus and Repository.
 * <p>
 * Axon delivers commands placed on the bus to the Aggregate. Axon supports the 'applying' of
 * Events to the Aggregate, and the handling of those events by the aggregate or any other
 * configured EventHandlers.
 */

public class Todo extends AbstractAnnotatedAggregateRoot {

    private static final Logger LOG = LoggerFactory.getLogger(Todo.class);

    /**
     * Aggregates that are managed by Axon must have a unique identifier.
     * Strategies similar to GUID are often used. The annotation 'AggregateIdentifier'
     * identifies the id field as such.
     */
    @AggregateIdentifier
    private String id;

    private String title;
    private boolean isDone = false;

    /**
     * This default constructor is used by the Repository to construct
     * a prototype Todo. Events are then used to set properties
     * such as the Todo's Id in order to make the Aggregate reflect
     * it's true logical state.
     */
    public Todo() {
    }

    /**
     * This constructor is marked as a 'CommandHandler' for the
     * CreateCommand. This command can be used to construct
     * new instances of the Aggregate. If successful a new TodoCreated
     * is 'applied' to the aggregate using the Axon 'apply' method. The apply
     * method appears to also propagate the Event to any other registered
     * 'Event Listeners', who may take further action.
     *
     * @param command
     */
    @CommandHandler
    public Todo(CreateCommand command) {
        LOG.debug("Command: 'CreateToDoItem' received.");
        apply(new TodoCreated(command.getTodoId(), command.getDescription()));
    }

    @CommandHandler
    public void markDone(MarkDoneCommand command) {
        LOG.debug("Command: 'MarkDone' received.");
        if (!this.isDone()) {
            apply(new TodoDone(id));
        } else {
            throw new IllegalStateException("This Todo (" + this.getId() + ") is already Done.");
        }
    }

    @CommandHandler
    public void markUndone(MarkUndoneCommand command) {
        LOG.debug("Command: 'MarkUndone' received.");
        if (this.isDone()) {
            apply(new TodoUndone(id));
        } else {
            throw new IllegalStateException("This Todo (" + this.getId() + ") is no longer Done.");
        }
    }

    /**
     * This method is marked as an EventSourcingHandler and is therefore used by the Axon framework to
     * handle events of the specified type (TodoCreated). The TodoCreated can be
     * raised either by the constructor during Todo(CreateCommand) or by the
     * Repository when 're-loading' the aggregate.
     *
     * @param event
     */
    @EventSourcingHandler
    public void on(TodoCreated event) {
        this.id = event.getId();
        this.title = event.getTitle();
        LOG.debug("Applied: 'TodoCreated' [{}] '{}'", event.getId(), event.getTitle());
    }

    @EventSourcingHandler
    public void on(TodoDone event) {
        this.isDone = true;
        LOG.debug("Applied: 'TodoItemCompletedEvent' [{}]", event.getId());
    }

    @EventSourcingHandler
    public void on(TodoUndone event) {
        this.isDone = false;
        LOG.debug("Applied: 'TodoUndone' [{}]", event.getId());
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isDone() {
        return isDone;
    }
}
