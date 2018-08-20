package com.soagrowers.productquery.configuration;

import org.axonframework.amqp.eventhandling.AMQPMessageConverter;
import org.axonframework.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.mongo.DefaultMongoTemplate;
import org.axonframework.mongo.MongoTemplate;
import org.axonframework.mongo.eventhandling.saga.repository.MongoSagaStore;
import org.axonframework.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.mongo.eventsourcing.eventstore.documentperevent.DocumentPerEventStorageStrategy;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.axonframework.spring.config.AnnotationDriven;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.mongodb.MongoClient;
import com.rabbitmq.client.Channel;

/**
 * Created by ben on 18/02/16.
 */
@Configuration
@AnnotationDriven
class AxonConfiguration {

    @Autowired
    public ConnectionFactory connectionFactory;

    @Autowired
    public PlatformTransactionManager transactionManager;

    @Autowired
    public String uniqueQueueName;

    @Autowired
    public MongoClient mongoClient;


    @Bean
    @Qualifier("eventSerializer")
    Serializer axonJsonSerializer() {
        return new JacksonSerializer();
    }
//    @Bean
//    ListenerContainerLifecycleManager listenerContainerLifecycleManager() {
//        ListenerContainerLifecycleManager listenerContainerLifecycleManager = new ListenerContainerLifecycleManager();
//        listenerContainerLifecycleManager.setConnectionFactory(connectionFactory);
//        return listenerContainerLifecycleManager;
//    }
//
//    @Bean
//    SpringAMQPConsumerConfiguration springAMQPConsumerConfiguration() {
//        SpringAMQPConsumerConfiguration amqpConsumerConfiguration = new SpringAMQPConsumerConfiguration();
//        amqpConsumerConfiguration.setTxSize(10);
//        amqpConsumerConfiguration.setTransactionManager(transactionManager);
//        amqpConsumerConfiguration.setQueueName(uniqueQueueName);
//        return amqpConsumerConfiguration;
//    }
//
//
//    @Bean
//    SimpleCluster simpleCluster(SpringAMQPConsumerConfiguration springAMQPConsumerConfiguration) {
//        SimpleCluster simpleCluster = new SimpleCluster(uniqueQueueName);
//        simpleCluster.getMetaData().setProperty(AMQP_CONFIG_KEY, springAMQPConsumerConfiguration);
//        return simpleCluster;
//    }
//
//    @Bean
//    EventBusTerminal terminal() {
//        SpringAMQPTerminal terminal = new SpringAMQPTerminal();
//        terminal.setConnectionFactory(connectionFactory);
//        //terminal.setSerializer(xmlSerializer());
//        terminal.setSerializer(axonJsonSerializer());
//        terminal.setExchangeName(terminalName);
//        terminal.setListenerContainerLifecycleManager(listenerContainerLifecycleManager());
//        terminal.setDurable(true);
//        terminal.setTransactional(true);
//        return terminal;
//    }
//
//    @Bean
//    EventBus eventBus(SimpleCluster simpleCluster) {
//        return new ClusteringEventBus(new DefaultClusterSelector(simpleCluster), terminal());
//    }
    
    @Bean
    public SpringAMQPMessageSource myQueueMessageSource(AMQPMessageConverter messageConverter) {
        return new SpringAMQPMessageSource(messageConverter) {
            
            @RabbitListener(queues = "#{uniqueQueueName}")
            @Override
            public void onMessage(Message message, Channel channel) {
                super.onMessage(message, channel);
            }
        };
    }      

    @Bean(name = "axonMongoTemplate")
    MongoTemplate axonMongoTemplate() {
        MongoTemplate template = new DefaultMongoTemplate(mongoClient);

        return template;
    }

    @Bean
    EventStore eventStore() {
        EmbeddedEventStore eventStore = new EmbeddedEventStore(eventStoreEngine());
        return eventStore;
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
