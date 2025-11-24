package com.example.educationalproject.kafka;

import com.example.educationalproject.dto.kafka.CurrencyRateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class CurrencyRateEventProducer {

    private final KafkaTemplate<String, CurrencyRateEvent> kafkaTemplate;

    @Value("${app.kafka.topics.currency-rate-created}")
    private String currencyRateCreatedTopic;

    @Value("${app.kafka.topics.currency-rate-updated}")
    private String currencyRateUpdatedTopic;

    @Value("${app.kafka.topics.currency-rate-deleted}")
    private String currencyRateDeletedTopic;

    public void sendCurrencyRateCreated(CurrencyRateEvent event) {
        sendEvent(currencyRateCreatedTopic, event, "created");
    }

    public void sendCurrencyRateUpdated(CurrencyRateEvent event) {
        sendEvent(currencyRateUpdatedTopic, event, "updated");
    }

    public void sendCurrencyRateDeleted(CurrencyRateEvent event) {
        sendEvent(currencyRateDeletedTopic, event, "deleted");
    }

    private void sendEvent(String topic, CurrencyRateEvent event, String eventType) {
        String key = event.getCurrencyCode() + "-" + event.getRateDate();

        CompletableFuture<SendResult<String, CurrencyRateEvent>> future = kafkaTemplate.send(topic, key, event);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.debug("Successfully sent {} event for currency: {}, offset: {}",
                        eventType, event.getCurrencyCode(), result.getRecordMetadata().offset());
            } else {
                log.error("Failed to send {} event for currency: {}, error: {}",
                        eventType, event.getCurrencyCode(), ex.getMessage());
            }
        });
    }
}
