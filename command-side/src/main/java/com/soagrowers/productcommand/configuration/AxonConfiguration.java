package com.soagrowers.productcommand.configuration;

import org.axonframework.amqp.eventhandling.AMQPMessageConverter;
import org.axonframework.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.mongo.DefaultMongoTemplate;
import org.axonframework.mongo.MongoTemplate;
import org.axonframework.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.mongo.eventsourcing.eventstore.documentperevent.DocumentPerEventStorageStrategy;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.rabbitmq.client.Channel;
import com.soagrowers.productcommand.aggregates.ProductAggregate;

/**
 * Created by ben on 23/02/16.
 */
@Configuration
// @AnnotationDriven
public class AxonConfiguration {

    private static final String AMQP_CONFIG_KEY = "AMQP.Config";

    @Autowired
    public Mongo mongo;
    
    @Autowired
    public MongoClient mongoClient;

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

    @Qualifier("eventSerializer")
    @Bean
    JacksonSerializer axonJsonSerializer() {
        return new JacksonSerializer();
    }
    
    @Bean
    ConnectionFactory amqpConnectionFactory() {
        return new CachingConnectionFactory();
    }

//    @Bean
//    ListenerContainerLifecycleManager listenerContainerLifecycleManager() {
//        ListenerContainerLifecycleManager mgr = new ListenerContainerLifecycleManager();
//        mgr.setConnectionFactory(connectionFactory);
//        return mgr;
//    }

//    @Bean
//    SpringAMQPConsumerConfiguration springAMQPConsumerConfiguration() {
//        SpringAMQPConsumerConfiguration cfg = new SpringAMQPConsumerConfiguration();
//        cfg.setTransactionManager(transactionManager);
//        cfg.setQueueName(queueName);
//        cfg.setTxSize(10);
//        return cfg;
//    }
//
//    @Bean
//    SimpleCluster simpleCluster() {
//        SimpleCluster cluster = new SimpleCluster(queueName);
//        cluster.getMetaData().setProperty(AMQP_CONFIG_KEY, springAMQPConsumerConfiguration());
//        return cluster;
//    }

//    @Bean
//    EventBusTerminal terminal() {
//        SpringAMQPTerminal terminal = new SpringAMQPTerminal();
//        terminal.setConnectionFactory(connectionFactory);
//        terminal.setExchangeName(exchangeName);
//        terminal.setDurable(true);
//        terminal.setTransactional(true);
//        terminal.setSerializer(axonJsonSerializer());
//        // terminal.setSerializer(xmlSerializer());
//        terminal.setListenerContainerLifecycleManager(listenerContainerLifecycleManager());
//        return terminal;
//    }

//    @Bean
//    EventBus eventBus() {
//        return new ClusteringEventBus(new DefaultClusterSelector(simpleCluster()), terminal());
//    }

    
    @Bean
    public SpringAMQPMessageSource myQueueMessageSource(AMQPMessageConverter messageConverter) {
        return new SpringAMQPMessageSource(messageConverter) {
            
            @RabbitListener(queues = "${spring.application.queue}")
            @Override
            public void onMessage(Message message, Channel channel) {
                super.onMessage(message, channel);
            }
        };
    }    
    
    @Bean(name = "axonMongoTemplate")
    MongoTemplate axonMongoTemplate() {
        MongoTemplate template = new DefaultMongoTemplate(mongoClient).withSnapshotCollection(snapshotCollectionName)
                .withDomainEventsCollection(eventsCollectionName);

//        MongoTemplate template = new DefaultMongoTemplate(mongo, databaseName, eventsCollectionName,
//                snapshotCollectionName, null, null);
        return template;
    }

    @Bean
    EventStore eventStore() {
        //MongoEventStore eventStore = new MongoEventStore(xmlSerializer(), axonMongoTemplate());
        EmbeddedEventStore eventStore = new EmbeddedEventStore(eventStoreEngine());
        return eventStore;
    }

    @Bean
    EventStorageEngine eventStoreEngine() {
//        // MongoEventStore eventStore = new MongoEventStore(xmlSerializer(),axonMongoTemplate());
//        MongoEventStore eventStore = new MongoEventStore(axonJsonSerializer(), axonMongoTemplate());
        return new MongoEventStorageEngine(axonJsonSerializer(), null,axonMongoTemplate(),new DocumentPerEventStorageStrategy());
    }

    @Bean
    EventSourcingRepository<ProductAggregate> productEventSourcingRepository() {
        EventSourcingRepository<ProductAggregate> repo = new EventSourcingRepository<>(ProductAggregate.class,
                eventStore());
//        repo.setEventBus(eventBus());
        return repo;
    }

//    @Bean
//    CommandBus commandBus() {
//        // SimpleCommandBus commandBus = new SimpleCommandBus();
//        return new SimpleCommandBus();
//    }

//    @Bean
//    CommandGatewayFactoryBean<CommandGateway> commandGatewayFactoryBean() {
//        CommandGatewayFactoryBean<CommandGateway> factory = new CommandGatewayFactoryBean<CommandGateway>();
//        factory.setCommandBus(commandBus());
//        return factory;
//    }

//    /**
//     * This method allows Axon to automatically find your @EventHandler's
//     *
//     * @return
//     */
//    @Bean
//    AnnotationEventListenerBeanPostProcessor eventListenerBeanPostProcessor() {
//        AnnotationEventListenerBeanPostProcessor proc = new AnnotationEventListenerBeanPostProcessor();
//        proc.setEventBus(eventBus());
//        return proc;
//    }

//    /**
//     * This method allows Axon to automatically find your @CommandHandler's
//     *
//     * @return
//     */
//    @Bean
//    AnnotationCommandHandlerBeanPostProcessor commandHandlerBeanPostProcessor() {
//        AnnotationCommandHandlerBeanPostProcessor proc = new AnnotationCommandHandlerBeanPostProcessor();
//        proc.setCommandBus(commandBus());
//        return proc;
//    }

//    /**
//     * This method registers your Aggregate Root as a @CommandHandler
//     *
//     * @return
//     */
//    @Bean
//    AggregateAnnotationCommandHandler<ProductAggregate> productAggregateCommandHandler() {
//        AggregateAnnotationCommandHandler<ProductAggregate> handler = new AggregateAnnotationCommandHandler<ProductAggregate>(
//                ProductAggregate.class, productEventSourcingRepository(), commandBus());
//        return handler;
//    }

}
