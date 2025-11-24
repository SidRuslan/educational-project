package com.example.educationalproject.kafka;

import com.example.educationalproject.dto.CurrencyRateResponse;
import com.example.educationalproject.dto.kafka.CurrencyRateEvent;
import com.example.educationalproject.mapper.CurrencyRateEventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class CurrencyRateEventPublisher {

    private final CurrencyRateEventProducer currencyRateEventProducer;
    private final CurrencyRateEventMapper currencyRateEventMapper;

    public void publishCurrencyRateCreated(CurrencyRateResponse response) {
        try {
            CurrencyRateEvent event = currencyRateEventMapper.toCreateEvent(response);
            currencyRateEventProducer.sendCurrencyRateCreated(event);
            log.debug("Published currency rate created event for: {}", response.getCurrencyCode());
        } catch (Exception ex) {
            log.error("Failed to publish currency rate created event for: {}", response.getCurrencyCode(), ex);
        }
    }

    public void publishCurrencyRateUpdated(CurrencyRateResponse response) {
        try {
            CurrencyRateEvent event = currencyRateEventMapper.toUpdatedEvent(response);
            currencyRateEventProducer.sendCurrencyRateUpdated(event);
            log.debug("Published currency rate updated event for: {}", response.getCurrencyCode());
        } catch (Exception ex) {
            log.error("Failed to publish currency rate updated event for: {}", response.getCurrencyCode(), ex);
        }
    }

    public void publishCurrencyRateDeleted(CurrencyRateResponse response) {
        try {
            CurrencyRateEvent event = currencyRateEventMapper.toDeletedEvent(response);

            currencyRateEventProducer.sendCurrencyRateDeleted(event);
            log.debug("Published currency rate deleted event for: {}", response.getCurrencyCode());
        } catch (Exception ex) {
            log.error("Failed to publish currency rate deleted event for: {}", response.getCurrencyCode(), ex);
        }
    }
}
