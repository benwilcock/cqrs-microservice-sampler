package com.soagrowers.productcommand.configuration;

import com.mongodb.Mongo;
import com.soagrowers.productcommand.aggregates.ProductAggregate;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.annotation.AggregateAnnotationCommandHandler;
import org.axonframework.commandhandling.annotation.AnnotationCommandHandlerBeanPostProcessor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.CommandGatewayFactoryBean;
import org.axonframework.contextsupport.spring.AnnotationDriven;
import org.axonframework.eventhandling.*;
import org.axonframework.eventhandling.amqp.spring.ListenerContainerLifecycleManager;
import org.axonframework.eventhandling.amqp.spring.SpringAMQPConsumerConfiguration;
import org.axonframework.eventhandling.amqp.spring.SpringAMQPTerminal;
import org.axonframework.eventhandling.annotation.AnnotationEventListenerBeanPostProcessor;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventstore.EventStore;
import org.axonframework.eventstore.mongo.DefaultMongoTemplate;
import org.axonframework.eventstore.mongo.MongoEventStore;
import org.axonframework.eventstore.mongo.MongoTemplate;
import org.axonframework.serializer.json.JacksonSerializer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ben on 23/02/16.
 */
@Configuration
@AnnotationDriven
public class AxonConfiguration {

    private static final String AMQP_CONFIG_KEY = "AMQP.Config";

    @Autowired
    public Mongo mongo;

    @Autowired
    public ConnectionFactory connectionFactory;

    @Autowired
    public RabbitTransactionManager transactionManager;

    @Value("${spring.application.queue}")
    private String queueName;

    @Value("${spring.application.exchange}")
    private String exchangeName;

    @Value("${spring.application.databaseName}")
    private String databaseName;

    @Value("${spring.application.eventsCollectionName}")
    private String eventsCollectionName;

    @Value("${spring.application.snapshotCollectionName}")
    private String snapshotCollectionName;

/*    @Bean
    XStreamSerializer xmlSerializer() {
        return new XStreamSerializer();
    }*/

    @Bean
    JacksonSerializer jsonSerializer() {
        return new JacksonSerializer();
    }

    @Bean
    ListenerContainerLifecycleManager listenerContainerLifecycleManager() {
        ListenerContainerLifecycleManager mgr = new ListenerContainerLifecycleManager();
        mgr.setConnectionFactory(connectionFactory);
        return mgr;
    }

    @Bean
    SpringAMQPConsumerConfiguration springAMQPConsumerConfiguration() {
        SpringAMQPConsumerConfiguration cfg = new SpringAMQPConsumerConfiguration();
        cfg.setTransactionManager(transactionManager);
        cfg.setQueueName(queueName);
        cfg.setTxSize(10);
        return cfg;
    }


    @Bean
    SimpleCluster simpleCluster() {
        SimpleCluster cluster = new SimpleCluster(queueName);
        cluster.getMetaData().setProperty(AMQP_CONFIG_KEY, springAMQPConsumerConfiguration());
        return cluster;
    }

    @Bean
    EventBusTerminal terminal() {
        SpringAMQPTerminal terminal = new SpringAMQPTerminal();
        terminal.setConnectionFactory(connectionFactory);
        terminal.setExchangeName(exchangeName);
        terminal.setDurable(true);
        terminal.setTransactional(true);
        terminal.setSerializer(jsonSerializer());
        //terminal.setSerializer(xmlSerializer());
        terminal.setListenerContainerLifecycleManager(listenerContainerLifecycleManager());
        return terminal;
    }

    @Bean
    EventBus eventBus() {
        return new ClusteringEventBus(new DefaultClusterSelector(simpleCluster()), terminal());
    }

    @Bean(name = "axonMongoTemplate")
    MongoTemplate axonMongoTemplate() {
        MongoTemplate template = new DefaultMongoTemplate(mongo,
                databaseName, eventsCollectionName, snapshotCollectionName, null, null);
        return template;
    }

    @Bean
    EventStore eventStore() {
        //MongoEventStore eventStore = new MongoEventStore(xmlSerializer(), axonMongoTemplate());
        MongoEventStore eventStore = new MongoEventStore(jsonSerializer(), axonMongoTemplate());
        return eventStore;
    }

    @Bean
    EventSourcingRepository<ProductAggregate> productEventSourcingRepository() {
        EventSourcingRepository<ProductAggregate> repo = new EventSourcingRepository<ProductAggregate>(ProductAggregate.class, eventStore());
        repo.setEventBus(eventBus());
        return repo;
    }

    @Bean
    CommandBus commandBus() {
        SimpleCommandBus commandBus = new SimpleCommandBus();
        return commandBus;
    }

    @Bean
    CommandGatewayFactoryBean<CommandGateway> commandGatewayFactoryBean() {
        CommandGatewayFactoryBean<CommandGateway> factory = new CommandGatewayFactoryBean<CommandGateway>();
        factory.setCommandBus(commandBus());
        return factory;
    }

    /**
     * This method allows Axon to automatically find your @EventHandler's
     *
     * @return
     */
    @Bean
    AnnotationEventListenerBeanPostProcessor eventListenerBeanPostProcessor() {
        AnnotationEventListenerBeanPostProcessor proc = new AnnotationEventListenerBeanPostProcessor();
        proc.setEventBus(eventBus());
        return proc;
    }

    /**
     * This method allows Axon to automatically find your @CommandHandler's
     *
     * @return
     */
    @Bean
    AnnotationCommandHandlerBeanPostProcessor commandHandlerBeanPostProcessor() {
        AnnotationCommandHandlerBeanPostProcessor proc = new AnnotationCommandHandlerBeanPostProcessor();
        proc.setCommandBus(commandBus());
        return proc;
    }

    /**
     * This method registers your Aggregate Root as a @CommandHandler
     *
     * @return
     */
    @Bean
    AggregateAnnotationCommandHandler<ProductAggregate> productAggregateCommandHandler() {
        AggregateAnnotationCommandHandler<ProductAggregate> handler = new AggregateAnnotationCommandHandler<ProductAggregate>(
                ProductAggregate.class,
                productEventSourcingRepository(),
                commandBus());
        return handler;
    }

}
