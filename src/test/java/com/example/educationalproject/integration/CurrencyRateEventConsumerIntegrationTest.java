package com.example.educationalproject.integration;

import com.example.educationalproject.dto.kafka.CurrencyRateEvent;
import com.example.educationalproject.kafka.CurrencyRateEventConsumer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest
@ActiveProfiles("test")
public class CurrencyRateEventConsumerIntegrationTest {

    @Autowired
    private CurrencyRateEventConsumer currencyRateEventConsumer;

    @Test
    void handleCurrencyRateCreated_WithSpringContext_ShouldWork() {
        CurrencyRateEvent event = createTestEvent(CurrencyRateEvent.EventType.CREATED);

        assertThatNoException()
                .isThrownBy(() -> currencyRateEventConsumer.handleCurrencyRateCreated(event));
    }

    @Test
    void handleCurrencyRateUpdated_WithSpringContext_ShouldWork() {
        CurrencyRateEvent event = createTestEvent(CurrencyRateEvent.EventType.UPDATED);

        assertThatNoException()
                .isThrownBy(() -> currencyRateEventConsumer.handleCurrencyRateUpdated(event));
    }

    @Test
    void handleCurrencyRateDeleted_WithSpringContext_ShouldWork() {
        CurrencyRateEvent event = createTestEvent(CurrencyRateEvent.EventType.DELETED);

        assertThatNoException()
                .isThrownBy(() -> currencyRateEventConsumer.handleCurrencyRateDeleted(event));
    }

    private CurrencyRateEvent createTestEvent(CurrencyRateEvent.EventType eventType) {
        return new CurrencyRateEvent(
                UUID.fromString("abb7b36d-39f3-455b-8741-8d034a74ceb7"),
                "USD",
                "Test Currency",
                new BigDecimal("75.5000"),
                LocalDate.of(2025, 11, 22),
                LocalDateTime.now(),
                LocalDateTime.now(),
                eventType,
                LocalDateTime.now()
        );
    }
}
