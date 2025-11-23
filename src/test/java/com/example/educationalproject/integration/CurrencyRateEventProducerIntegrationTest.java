package com.example.educationalproject.integration;

import com.example.educationalproject.dto.kafka.CurrencyRateEvent;
import com.example.educationalproject.kafka.CurrencyRateEventProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class CurrencyRateEventProducerIntegrationTest {

    @Autowired
    private CurrencyRateEventProducer currencyRateEventProducer;

    @MockBean
    private KafkaTemplate<String, CurrencyRateEvent> kafkaTemplate;

    @BeforeEach
    void setUp() {
        CompletableFuture<SendResult<String, CurrencyRateEvent>> mockFuture =
                CompletableFuture.completedFuture(mock(SendResult.class));

        when(kafkaTemplate.send(anyString(), anyString(), any(CurrencyRateEvent.class)))
                .thenReturn(mockFuture);
    }

    @Test
    void sendCurrencyRateCreated_WithSpringContext_ShouldWork() {
        CurrencyRateEvent event = createTestEvent(CurrencyRateEvent.EventType.CREATED);

        currencyRateEventProducer.sendCurrencyRateCreated(event);

        verify(kafkaTemplate).send(eq("currency-rate-created"), anyString(), eq(event));
    }

    @Test
    void sendCurrencyRateUpdated_WithSpringContext_ShouldWork() {
        CurrencyRateEvent event = createTestEvent(CurrencyRateEvent.EventType.UPDATED);

        currencyRateEventProducer.sendCurrencyRateUpdated(event);

        verify(kafkaTemplate).send(eq("currency-rate-updated"), anyString(), eq(event));
    }

    @Test
    void sendCurrencyRateDeleted_WithSpringContext_ShouldWork() {
        CurrencyRateEvent event = createTestEvent(CurrencyRateEvent.EventType.DELETED);

        currencyRateEventProducer.sendCurrencyRateDeleted(event);

        verify(kafkaTemplate).send(eq("currency-rate-deleted"), anyString(), eq(event));
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
