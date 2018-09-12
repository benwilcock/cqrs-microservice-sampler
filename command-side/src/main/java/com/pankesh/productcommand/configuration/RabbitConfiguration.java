package com.pankesh.productcommand.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfiguration {

    @Value("${spring.rabbitmq.hostname}")
    private String hostname;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.application.exchange}")
    private String exchangeName;

    @Value("${spring.application.queue}")
    private String queueName;

    @Bean
    Queue defaultStream() {
        return new Queue(queueName, true);
    }

    @Bean
    FanoutExchange eventBusExchange() {
        return new FanoutExchange(exchangeName, true, false);
    }

    @Bean
    Binding binding() {
        return new Binding(queueName, Binding.DestinationType.QUEUE, exchangeName, "*.*", null);
    }

    @Bean
    ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(hostname);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    @Bean
    @Required
    RabbitAdmin rabbitAdmin() {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory());
        admin.setAutoStartup(true);
        admin.declareExchange(eventBusExchange());
        admin.declareQueue(defaultStream());
        admin.declareBinding(binding());
        return admin;
    }

    @Bean
    RabbitTransactionManager transactionManager(){
        RabbitTransactionManager txMgr = new RabbitTransactionManager(connectionFactory());
        return txMgr;
    }
}
