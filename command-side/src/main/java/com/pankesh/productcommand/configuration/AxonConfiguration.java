package com.pankesh.productcommand.configuration;


import com.pankesh.productcommand.aggregates.ProductAggregate;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.common.jpa.ContainerManagedEntityManagerProvider;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventhandling.saga.repository.SagaStore;
import org.axonframework.eventhandling.saga.repository.jpa.JpaSagaStore;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventhandling.tokenstore.jpa.JpaTokenStore;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.eventsourcing.eventstore.jpa.JpaEventStorageEngine;
import org.axonframework.messaging.annotation.ParameterResolverFactory;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.axonframework.serialization.xml.XStreamSerializer;
import org.axonframework.spring.eventsourcing.SpringAggregateSnapshotter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


@Configuration
public class AxonConfiguration {


    private static final Logger LOG = LoggerFactory.getLogger(AxonConfiguration.class);


    @Value("${spring.application.queue}")
    private String queueName;


    @Value("${spring.application.databaseName}")
    private String databaseName;

    @Value("${spring.application.eventsCollectionName}")
    private String eventsCollectionName;

    @Value("${spring.application.snapshotCollectionName}")
    private String snapshotCollectionName;

    @Value("${spring.application.sagaCollectionName}")
    private String sagaCollectionName;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private TransactionManager transactionManager;


    @Qualifier("eventSerializer")
    @Bean
    Serializer axonJsonSerializer() {
        return new JacksonSerializer();
    }

    @Bean
    EventStore eventStore() {

        EmbeddedEventStore eventStore = new EmbeddedEventStore(eventStoreEngine());
        return eventStore;
    }

    @Bean
    SagaStore sagaStore() {
        return new JpaSagaStore(new XStreamSerializer(), entityManagerProvider());
        //return new MongoSagaStore(axonMongoTemplate(), new XStreamSerializer());
    }

    @Bean
    EntityManagerProvider entityManagerProvider() {
        ContainerManagedEntityManagerProvider entityManagerProvider = new ContainerManagedEntityManagerProvider();
        entityManagerProvider.setEntityManager(entityManagerFactory.createEntityManager());
        return entityManagerProvider;
    }

    @Bean
    EventStorageEngine eventStoreEngine() {
        try {

            return new JpaEventStorageEngine(new XStreamSerializer(), null, dataSource, axonJsonSerializer(), entityManagerProvider(), transactionManager);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }


    }


    @Bean
    public TokenStore tokenStore(EntityManagerProvider entityManagerProvider) {
        return new JpaTokenStore(entityManagerProvider, axonJsonSerializer());
        //return new MongoTokenStore(axonMongoTemplate(), axonJsonSerializer());
    }


    @Bean
    public SpringAggregateSnapshotter snapshotter(ParameterResolverFactory parameterResolverFactory) {
        Executor executor = Executors.newSingleThreadExecutor();
        return new SpringAggregateSnapshotter(eventStore(), parameterResolverFactory, executor, transactionManager);

    }

    @Bean
    public SnapshotTriggerDefinition snapshotTriggerDefinition(SpringAggregateSnapshotter snapshotter) {
        return new EventCountSnapshotTriggerDefinition(snapshotter, 2);
    }

    @Bean
    @Autowired
    public Repository<ProductAggregate> productAggregateRepository(SnapshotTriggerDefinition snapshotTriggerDefinition) {
        EventSourcingRepository<ProductAggregate> repo = new EventSourcingRepository(ProductAggregate.class, eventStore(), snapshotTriggerDefinition);
        return repo;
    }

}
