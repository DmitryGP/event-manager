package org.dgp.eventmanager.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.StringSerializer;
import org.dgp.eventmanager.notifications.EditEventMessage;
import org.dgp.eventmanager.notifications.NearestEventsNotificationMessage;
import org.dgp.eventmanager.services.EditEventNotificationSender;
import org.dgp.eventmanager.services.NearestEventsNotificationSender;
import org.dgp.eventmanager.services.impl.EditEventNotificationSenderKafkaImpl;
import org.dgp.eventmanager.services.impl.NearestEventsNotificationSenderKafkaImpl;
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

@Configuration
public class KafkaConfiguration {

    public final String editEventNotificationTopicName;

    public final String nearestEventsNotificationTopicName;

    public KafkaConfiguration(
            @Value("${application.kafka.edit-event-notification-topic}") String editEventNotificationTopicName,
            @Value("${application.kafka.nearest-events-notification-topic}") String nearestEventsNotificationTopicName) {
        this.editEventNotificationTopicName = editEventNotificationTopicName;
        this.nearestEventsNotificationTopicName = nearestEventsNotificationTopicName;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return JacksonUtils.enhancedObjectMapper();
    }

    @Bean
    public ProducerFactory<String, EditEventMessage> editEventMessageProducerFactory(
            KafkaProperties kafkaProperties, ObjectMapper mapper) {
        var props = kafkaProperties.buildProducerProperties(null);
        props.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        var kafkaProducerFactory = new DefaultKafkaProducerFactory<String, EditEventMessage>(props);
        kafkaProducerFactory.setValueSerializer(new JsonSerializer<>(mapper));

        return kafkaProducerFactory;
    }

    @Bean
    public KafkaTemplate<String, EditEventMessage> editEventMessagekafkaTemplate(
            ProducerFactory<String, EditEventMessage> editEventMessageProducerFactory) {
        return new KafkaTemplate<>(editEventMessageProducerFactory);
    }

    @Bean
    public NewTopic editEventNotificationTopic() {
        return TopicBuilder
                .name(editEventNotificationTopicName)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public EditEventNotificationSender editEventNotificationSender(
            NewTopic editEventNotificationTopic,
            KafkaTemplate<String, EditEventMessage> editEventMessagekafkaTemplate) {

        return new EditEventNotificationSenderKafkaImpl(
                editEventNotificationTopic.name(),
                editEventMessagekafkaTemplate);
    }

    @Bean
    public ProducerFactory<String, NearestEventsNotificationMessage> nearestEventsNotificationMessageProducerFactory(
            KafkaProperties kafkaProperties, ObjectMapper mapper) {
        var props = kafkaProperties.buildProducerProperties(null);
        props.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        var kafkaProducerFactory = new DefaultKafkaProducerFactory<String, NearestEventsNotificationMessage>(props);
        kafkaProducerFactory.setValueSerializer(new JsonSerializer<>(mapper));

        return kafkaProducerFactory;
    }

    @Bean
    public KafkaTemplate<String, NearestEventsNotificationMessage> nearestEventsNotificationMessagekafkaTemplate(
            ProducerFactory<String, NearestEventsNotificationMessage> nearestEventsNotificationMessageProducerFactory) {
        return new KafkaTemplate<>(nearestEventsNotificationMessageProducerFactory);
    }

    @Bean
    public NewTopic nearestEventNotificationTopic() {
        return TopicBuilder
                .name(nearestEventsNotificationTopicName)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NearestEventsNotificationSender nearestEventsNotificationSender(
            NewTopic nearestEventNotificationTopic,
            KafkaTemplate<String, NearestEventsNotificationMessage> nearestEventsNotificationMessagekafkaTemplate) {
        return new NearestEventsNotificationSenderKafkaImpl(
                nearestEventNotificationTopic.name(),
                nearestEventsNotificationMessagekafkaTemplate);
    }
}