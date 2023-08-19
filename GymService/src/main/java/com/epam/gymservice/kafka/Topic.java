package com.epam.gymservice.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class Topic {

    @Bean
    public NewTopic setUserTopic() {
        return TopicBuilder.name("notifications").build();
    }
    @Bean
    public NewTopic setTrainingTopic() {
        return TopicBuilder.name("Trainings").build();
    }
}
