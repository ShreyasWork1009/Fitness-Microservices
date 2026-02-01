package com.example.activityservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.name}")
    private String queueName;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    // 1. Define the Queue
    @Bean
    public Queue activityQueue() {
        return new Queue(queueName, true); // true = durable
    }

    // 2. Define the Exchange (TopicExchange is flexible for routing)
    @Bean
    public DirectExchange activityExchange() {
        return new DirectExchange(exchangeName);
    }

    // 3. Bind the Queue to the Exchange using the Routing Key
    @Bean
    public Binding activityBinding(Queue queue, DirectExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(routingKey);
    }

    // 4. Message Converter for JSON
    @Bean
    public MessageConverter jsonConverter() {
        return new Jackson2JsonMessageConverter();
    }
}