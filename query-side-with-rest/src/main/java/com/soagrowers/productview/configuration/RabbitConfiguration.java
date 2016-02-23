package com.soagrowers.productview.configuration;

import org.axonframework.contextsupport.spring.AnnotationDriven;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ben on 19/02/16.
 */
@Configuration
@AnnotationDriven
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
    Queue eventStream() {
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
        admin.declareQueue(eventStream());
        admin.declareBinding(binding());
        return admin;
    }
}
