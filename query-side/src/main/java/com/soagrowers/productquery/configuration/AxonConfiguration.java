package com.soagrowers.productquery.configuration;

import org.axonframework.amqp.eventhandling.AMQPMessageConverter;
import org.axonframework.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.mongo.DefaultMongoTemplate;
import org.axonframework.mongo.MongoTemplate;
import org.axonframework.mongo.eventhandling.saga.repository.MongoSagaStore;
import org.axonframework.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.mongo.eventsourcing.eventstore.documentperevent.DocumentPerEventStorageStrategy;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.mongodb.MongoClient;
import com.rabbitmq.client.Channel;

/**
 * Created by ben on 18/02/16.
 */
@Configuration
class AxonConfiguration {

    @Autowired
    public ConnectionFactory connectionFactory;

    @Autowired
    public PlatformTransactionManager transactionManager;
    
    @Value("${spring.application.databaseName}")
    private String databaseName;

    @Autowired
    public String uniqueQueueName;

    @Autowired
    public MongoClient mongoClient;

    @Bean
    @Qualifier("eventSerializer")
    Serializer axonJsonSerializer() {
        return new JacksonSerializer();
    }

    @Bean
    public SpringAMQPMessageSource queryMessageQueue(AMQPMessageConverter messageConverter) {
        SpringAMQPMessageSource springAMQPMessageSource = new SpringAMQPMessageSource(messageConverter) {

            @RabbitListener(queues = "#{uniqueQueueName}")
            @Override
            public void onMessage(Message message, Channel channel) {
                super.onMessage(message, channel);
            }
        };
        
        return springAMQPMessageSource;
        
        
    }

    @Bean(name = "axonMongoTemplate")
    MongoTemplate axonMongoTemplate() {
        MongoTemplate template = new DefaultMongoTemplate(mongoClient, databaseName);

        return template;
    }

    @Bean
    EventStorageEngine eventStoreEngine() {
        return new MongoEventStorageEngine(axonJsonSerializer(), null, axonMongoTemplate(),
                new DocumentPerEventStorageStrategy());
    }

    @Bean
    public MongoSagaStore sagaStore(Serializer eventSerializer, EntityManagerProvider entityManagerProvider) {
        return new MongoSagaStore(axonMongoTemplate(), axonJsonSerializer());
    }

}
