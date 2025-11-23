package com.example.educationalproject.kafka;

import com.example.educationalproject.dto.kafka.CurrencyRateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CurrencyRateEventConsumer {

    @KafkaListener(topics = "${app.kafka.topics.currency-rate-created}")
    public void handleCurrencyRateCreated(CurrencyRateEvent event) {
        log.info("Received currency rate created event: {} - {}",
                event.getCurrencyCode(), event.getExchangeRate());
    }

    @KafkaListener(topics = "${app.kafka.topics.currency-rate-updated}")
    public void handleCurrencyRateUpdated(CurrencyRateEvent event) {
        log.info("Received currency rate updated event: {} - {}",
                event.getCurrencyCode(), event.getExchangeRate());
    }

    @KafkaListener(topics = "${app.kafka.topics.currency-rate-deleted}")
    public void handleCurrencyRateDeleted(CurrencyRateEvent event) {
        log.info("Received currency rate deleted event: {} - {}",
                event.getCurrencyCode(), event.getId());
    }
}
