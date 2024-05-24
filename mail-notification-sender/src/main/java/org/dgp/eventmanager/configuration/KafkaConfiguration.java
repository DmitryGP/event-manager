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
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.support.JacksonUtils;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_RECORDS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;

@Configuration
public class KafkaConfiguration {

    public final String topicName;

    public KafkaConfiguration(@Value("${application.kafka.edit-event-notification-topic}")String topicName) {
        this.topicName = topicName;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return JacksonUtils.enhancedObjectMapper();
    }

    @Bean
    public RecordMessageConverter multiTypeConverter() {
        StringJsonMessageConverter converter = new StringJsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();

        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);
        typeMapper.addTrustedPackages("org.dgp.eventmanager.notifications");

        Map<String, Class<?>> mappings = new HashMap<>();
        mappings.put("editNotification", EditEventMessage.class);
        mappings.put("nearestEventNotification", NearestEventsNotificationMessage.class);

        typeMapper.setIdClassMapping(mappings);
        converter.setTypeMapper(typeMapper);

        return converter;
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory(
            KafkaProperties kafkaProperties) {
        var props = kafkaProperties.buildConsumerProperties(null);
        props.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        /*props.put(TYPE_MAPPINGS,
                "editNotification:org.dgp.eventmanager.notifications.EditEventMessage");*/
        props.put(MAX_POLL_RECORDS_CONFIG, 3);
        props.put(MAX_POLL_INTERVAL_MS_CONFIG, 3_000);

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object>
                    multiTypeKafkaListenerContainerFactory(KafkaProperties kafkaProperties) {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory(kafkaProperties));
        factory.setRecordMessageConverter(multiTypeConverter());
        factory.setBatchListener(false);
        factory.setConcurrency(1);
        factory.getContainerProperties().setIdleBetweenPolls(1_000);
        factory.getContainerProperties().setPollTimeout(1_000);

        return factory;
    }

    /*@Bean("listenerContainerFactory")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, EditEventMessage>>
    listenerContainerFactory(ConsumerFactory<String, EditEventMessage> consumerFactory) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, EditEventMessage>();
        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(true);
        factory.setConcurrency(1);
        factory.getContainerProperties().setIdleBetweenPolls(1_000);
        factory.getContainerProperties().setPollTimeout(1_000);

        var executor = new SimpleAsyncTaskExecutor("k-consumer-");
        executor.setConcurrencyLimit(10);
        var listenerTaskExecutor = new ConcurrentTaskExecutor(executor);
        factory.getContainerProperties().setListenerTaskExecutor(listenerTaskExecutor);
        return factory;
    }*/

    @Bean
    public CommonErrorHandler commonErrorHandler() {
        return new KafkaErrorHandler();
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
    public KafkaClient kafkaClient(MailSender mailSender) {
        return new KafkaClient(mailSender);
    }

}
