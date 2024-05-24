package org.dgp.eventmanager.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.dgp.eventmanager.external.EditEventMessage;
import org.dgp.eventmanager.services.EditEventNotificationSender;
import org.dgp.eventmanager.services.impl.EditEventNotificationSenderKafkaImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.JacksonUtils;
import org.springframework.kafka.support.serializer.JsonSerializer;

import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;
import static org.springframework.kafka.support.serializer.JsonSerializer.TYPE_MAPPINGS;

@Configuration
public class KafkaConfiguration {

    public final String topicName;

    public KafkaConfiguration(@Value("${application.kafka.edit-event-notification-topic}") String topicName) {
        this.topicName = topicName;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return JacksonUtils.enhancedObjectMapper();
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory(
            KafkaProperties kafkaProperties, ObjectMapper mapper) {
        var props = kafkaProperties.buildProducerProperties(null);
        props.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(TYPE_MAPPINGS, "editNotification:org.dgp.eventmanager.external.EditEventMessage," +
                "nearestEventNotification:org.dgp.eventmanager.external.NearestEventsNotificationMessage");
        var kafkaProducerFactory = new DefaultKafkaProducerFactory<String, Object>(props);
        kafkaProducerFactory.setValueSerializer(new JsonSerializer<>(mapper));
        return kafkaProducerFactory;
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(
            ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public NewTopic topic() {
        return TopicBuilder
                .name(topicName)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public EditEventNotificationSender editEventNotificationSender(
            NewTopic topic,
            KafkaTemplate<String, Object> kafkaTemplate) {

        return new EditEventNotificationSenderKafkaImpl(topic.name(), kafkaTemplate);
    }
}