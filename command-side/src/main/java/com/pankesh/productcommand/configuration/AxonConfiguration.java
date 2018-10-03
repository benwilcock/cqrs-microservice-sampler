package com.pankesh.productcommand.configuration;


import org.axonframework.commandhandling.model.Repository;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventhandling.saga.repository.SagaStore;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventsourcing.*;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.messaging.annotation.ParameterResolverFactory;
import org.axonframework.mongo.DefaultMongoTemplate;
import org.axonframework.mongo.MongoTemplate;
import org.axonframework.mongo.eventhandling.saga.repository.MongoSagaStore;
import org.axonframework.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.mongo.eventsourcing.eventstore.documentperevent.DocumentPerEventStorageStrategy;
import org.axonframework.mongo.eventsourcing.tokenstore.MongoTokenStore;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.axonframework.serialization.xml.XStreamSerializer;
import org.axonframework.spring.eventsourcing.SpringAggregateSnapshotter;
import org.axonframework.spring.eventsourcing.SpringAggregateSnapshotterFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.pankesh.productcommand.aggregates.ProductAggregate;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


@Configuration
public class AxonConfiguration {


    private static final Logger LOG = LoggerFactory.getLogger(AxonConfiguration.class);

    @Autowired
    public Mongo mongo;

    @Autowired
    public MongoClient mongoClient;

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

    @Value("${spring.application.sagaCollectionName}")
    private String sagaCollectionName;


    @Qualifier("eventSerializer")
    @Bean
    Serializer axonJsonSerializer() {
        return new JacksonSerializer();
    }

    @Bean(name = "axonMongoTemplate")
    MongoTemplate axonMongoTemplate() {
        MongoTemplate template = new DefaultMongoTemplate(mongoClient, databaseName)
                .withSnapshotCollection(snapshotCollectionName).withDomainEventsCollection(eventsCollectionName).withSagasCollection(sagaCollectionName);

        return template;
    }
    @Bean
    EventStore eventStore() {

        EmbeddedEventStore eventStore = new EmbeddedEventStore(eventStoreEngine());
        return eventStore;
    }

    @Bean
    SagaStore sagaStore(){
        return new MongoSagaStore(axonMongoTemplate(), new XStreamSerializer());
    }


    @Bean
    EventStorageEngine eventStoreEngine() {
        return new MongoEventStorageEngine(new XStreamSerializer(), null, axonJsonSerializer(), axonMongoTemplate(), new DocumentPerEventStorageStrategy());

    }

    @Bean
    public TokenStore tokenStore() {
        return new MongoTokenStore(axonMongoTemplate(), axonJsonSerializer());
    }


    @Bean
    public SpringAggregateSnapshotter snapshotter (TransactionManager txManager, ParameterResolverFactory parameterResolverFactory){
        Executor executor = Executors.newSingleThreadExecutor();
        return new SpringAggregateSnapshotter(eventStore(),parameterResolverFactory,executor,txManager);
        // return new AggregateSnapshotter(eventStore,productAggregateFactory);
    }

    @Bean
    public SnapshotTriggerDefinition snapshotTriggerDefinition(SpringAggregateSnapshotter snapshotter){
        return new EventCountSnapshotTriggerDefinition(snapshotter,1);
    }

    @Bean
    @Autowired
    public Repository<ProductAggregate> productAggregateRepository(SnapshotTriggerDefinition snapshotTriggerDefinition) {
        EventSourcingRepository<ProductAggregate> repo = new EventSourcingRepository(ProductAggregate.class,eventStore(),snapshotTriggerDefinition);
        return repo;
    }

}
