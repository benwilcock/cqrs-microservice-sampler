package com.pankesh.productcommand.configuration;

import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.mongo.DefaultMongoTemplate;
import org.axonframework.mongo.MongoTemplate;
import org.axonframework.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.mongo.eventsourcing.eventstore.documentperevent.DocumentPerEventStorageStrategy;
import org.axonframework.mongo.eventsourcing.tokenstore.MongoTokenStore;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
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
import com.pankesh.productcommand.aggregates.ProductAggregate;

/**
 * Created by ben on 23/02/16.
 */
@Configuration
public class AxonConfiguration {

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
    Serializer axonJsonSerializer() {
        return new JacksonSerializer();
    }
    
    @Bean
    ConnectionFactory amqpConnectionFactory() {
        return new CachingConnectionFactory();
    }
    
    @Bean(name = "axonMongoTemplate")
    MongoTemplate axonMongoTemplate() {
        MongoTemplate template = new DefaultMongoTemplate(mongoClient,databaseName).withSnapshotCollection(snapshotCollectionName)
                .withDomainEventsCollection(eventsCollectionName);
        
        return template;
    }

    @Bean
    EventStore eventStore() {
        EmbeddedEventStore eventStore = new EmbeddedEventStore(eventStoreEngine());
        return eventStore;
    }

    @Bean
    EventStorageEngine eventStoreEngine() {
        return new MongoEventStorageEngine(axonJsonSerializer(), null,axonMongoTemplate(),new DocumentPerEventStorageStrategy());
    }

    @Bean
    public TokenStore tokenStore() {
        return new MongoTokenStore(axonMongoTemplate(), axonJsonSerializer());
    }

    @Bean
    EventSourcingRepository<ProductAggregate> productEventSourcingRepository() {
        EventSourcingRepository<ProductAggregate> repo = new EventSourcingRepository<>(ProductAggregate.class,
                eventStore());
        return repo;
    }

}
