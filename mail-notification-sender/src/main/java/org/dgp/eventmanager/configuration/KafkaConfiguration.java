package org.dgp.eventmanager.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.dgp.eventmanager.handlers.KafkaErrorHandler;
import org.dgp.eventmanager.notifications.EditEventMessage;
import org.dgp.eventmanager.notifications.NearestEventsNotificationMessage;
import org.dgp.eventmanager.services.MailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.JacksonUtils;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;

import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_RECORDS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;

@Configuration
public class KafkaConfiguration {

    public final String editEventNotificationTopicName;

    public final String nearestEventsNotificationTopicName;

    public KafkaConfiguration(
            @Value("${application.kafka.edit-event-notification-topic}")String editEventNotificationTopicName,
            @Value("${application.kafka.nearest-events-notification-topic}")String nearestEventsNotificationTopicName) {
        this.editEventNotificationTopicName = editEventNotificationTopicName;
        this.nearestEventsNotificationTopicName = nearestEventsNotificationTopicName;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return JacksonUtils.enhancedObjectMapper();
    }

    @Bean
    public SimpleAsyncTaskExecutor executor() {
        var executor = new SimpleAsyncTaskExecutor("k-consumer-");
        executor.setConcurrencyLimit(10);

        return executor;
    }

    @Bean
    public ConsumerFactory<String, EditEventMessage> editEventMessageConsumerFactory(
            KafkaProperties kafkaProperties) {
        var props = kafkaProperties.buildConsumerProperties(null);
        props.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(MAX_POLL_RECORDS_CONFIG, 1);
        props.put(MAX_POLL_INTERVAL_MS_CONFIG, 3_000);

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(EditEventMessage.class));
    }

    @Bean("editEventMessageListenerContainerFactory")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, EditEventMessage>>
    editEventMessageListenerContainerFactory(
            ConsumerFactory<String, EditEventMessage> editEventMessageConsumerFactory,
            SimpleAsyncTaskExecutor executor) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, EditEventMessage>();
        factory.setConsumerFactory(editEventMessageConsumerFactory);
        factory.setBatchListener(false);
        factory.setConcurrency(1);
        factory.getContainerProperties().setIdleBetweenPolls(1_000);
        factory.getContainerProperties().setPollTimeout(1_000);

        var listenerTaskExecutor = new ConcurrentTaskExecutor(executor);
        factory.getContainerProperties().setListenerTaskExecutor(listenerTaskExecutor);

        return factory;
    }

    @Bean
    public CommonErrorHandler commonErrorHandler() {
        return new KafkaErrorHandler();
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
    public ConsumerFactory<String, NearestEventsNotificationMessage> nearestEventsNotificationMessageConsumerFactory(
            KafkaProperties kafkaProperties) {
        var props = kafkaProperties.buildConsumerProperties(null);
        props.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(MAX_POLL_RECORDS_CONFIG, 1);
        props.put(MAX_POLL_INTERVAL_MS_CONFIG, 3_000);

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(NearestEventsNotificationMessage.class));
    }

    @Bean("nearestEventsNotificationMessageListenerContainerFactory")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, NearestEventsNotificationMessage>>
    nearestEventsNotificationMessageListenerContainerFactory(
            ConsumerFactory<String, NearestEventsNotificationMessage> nearestEventsNotificationMessageConsumerFactory,
            SimpleAsyncTaskExecutor executor) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, NearestEventsNotificationMessage>();
        factory.setConsumerFactory(nearestEventsNotificationMessageConsumerFactory);
        factory.setBatchListener(false);
        factory.setConcurrency(1);
        factory.getContainerProperties().setIdleBetweenPolls(1_000);
        factory.getContainerProperties().setPollTimeout(1_000);

        var listenerTaskExecutor = new ConcurrentTaskExecutor(executor);
        factory.getContainerProperties().setListenerTaskExecutor(listenerTaskExecutor);

        return factory;
    }

    @Bean
    public KafkaClient kafkaClient(MailSender mailSender) {
        return new KafkaClient(mailSender);
    }

}
