package org.dgp.eventmanager.handlers;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;

@Slf4j
public class KafkaErrorHandler implements CommonErrorHandler {

    @Override
    public boolean handleOne(Exception thrownException,
                             ConsumerRecord<?, ?> record, Consumer<?, ?> consumer,
                             MessageListenerContainer container) {
        return handle(thrownException, record, consumer);
    }

    private boolean handle(Exception exception,
                           ConsumerRecord<?, ?> record,
                           Consumer<?, ?> consumer) {
        log.atError()
                .setMessage("Error while consuming message: {}")
                .addArgument(record)
                .setCause(exception)
                .log();

        consumer.commitSync();

        return true;
    }
}
