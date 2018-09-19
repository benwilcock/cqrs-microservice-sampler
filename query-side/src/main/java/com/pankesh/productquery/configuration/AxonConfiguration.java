package com.pankesh.productquery.configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.axonframework.common.Registration;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.messaging.SubscribableMessageSource;
import org.axonframework.mongo.DefaultMongoTemplate;
import org.axonframework.mongo.MongoTemplate;
import org.axonframework.mongo.eventhandling.saga.repository.MongoSagaStore;
import org.axonframework.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.mongo.eventsourcing.eventstore.documentperevent.DocumentPerEventStorageStrategy;
import org.axonframework.mongo.eventsourcing.tokenstore.MongoTokenStore;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.axonframework.spring.messaging.InboundEventMessageChannelAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.PlatformTransactionManager;

import com.mongodb.MongoClient;

import java.util.function.Consumer;


@Configuration
class AxonConfiguration {


    @Autowired
    public PlatformTransactionManager transactionManager;
    
    @Value("${spring.application.databaseName}")
    private String databaseName;

    @Autowired
    public MongoClient mongoClient;

    @Bean
    @Qualifier("eventSerializer")
    @Order(Ordered.HIGHEST_PRECEDENCE)
    Serializer axonJsonSerializer() {
        return new JacksonSerializer();
    }

    @Primary
    @Bean
    @Qualifier("messageSerializer")
    @Order(Ordered.HIGHEST_PRECEDENCE)
    Serializer axonMessageJsonSerializer() {
        return new JacksonSerializer(objectMapper());
    }


    @Primary
    @Bean
    @Qualifier("serializer")
    @Order(Ordered.HIGHEST_PRECEDENCE)
    Serializer axonSerializer() {
        return new JacksonSerializer(objectMapper());
    }

    @Qualifier("mapper")
    @Bean
    public ObjectMapper objectMapper() {
        return
                new ObjectMapper()
                        .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
        .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @Bean(name = "axonMongoTemplate")
    MongoTemplate axonMongoTemplate() {
        MongoTemplate template = new DefaultMongoTemplate(mongoClient, databaseName);

        return template;
    }
   /* @Qualifier("kafkaMessageSource")
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    org.axonframework.messaging.SubscribableMessageSource kafkaMessageSource() {
        return new InboundEventMessageChannelAdapter();
    }*/

    @Bean
    EventStorageEngine eventStoreEngine() {
        return new MongoEventStorageEngine(axonJsonSerializer(), null, axonMongoTemplate(),
                new DocumentPerEventStorageStrategy());
    }
    
    @Bean
    public MongoSagaStore sagaStore(@Qualifier("eventSerializer") Serializer eventSerializer, EntityManagerProvider entityManagerProvider) {
        return new MongoSagaStore(axonMongoTemplate(), axonMessageJsonSerializer());
    }


    @Bean
    public TokenStore tokenStore() {
        return new MongoTokenStore(axonMongoTemplate(), axonMessageJsonSerializer());
    }

}
