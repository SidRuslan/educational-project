package com.example.educationalproject.kafka;

import com.example.educationalproject.dto.kafka.CurrencyRateEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurrencyRateEventProducerTest {

    @Mock
    private KafkaTemplate<String, CurrencyRateEvent> kafkaTemplate;
    @InjectMocks
    private CurrencyRateEventProducer currencyRateEventProducer;

    private void setupTopics() throws Exception {
        var createdFiled = CurrencyRateEventProducer.class.getDeclaredField("currencyRateCreatedTopic");
        createdFiled.setAccessible(true);
        createdFiled.set(currencyRateEventProducer, "currency-rate-created");

        var updatedField = CurrencyRateEventProducer.class.getDeclaredField("currencyRateUpdatedTopic");
        updatedField.setAccessible(true);
        updatedField.set(currencyRateEventProducer, "currency-rate-updated");

        var deletedField = CurrencyRateEventProducer.class.getDeclaredField("currencyRateDeletedTopic");
        deletedField.setAccessible(true);
        deletedField.set(currencyRateEventProducer, "currency-rate-deleted");
    }

    @Test
    void sendCurrencyRateCreated_ShouldSendToCorrectTopic() throws Exception {
        setupTopics();
        CurrencyRateEvent event = createTestEvent(CurrencyRateEvent.EventType.CREATED);
        CompletableFuture<SendResult<String, CurrencyRateEvent>> future = CompletableFuture.completedFuture(null);

        when(kafkaTemplate.send(anyString(), anyString(), any(CurrencyRateEvent.class))).thenReturn(future);

        currencyRateEventProducer.sendCurrencyRateCreated(event);

        verify(kafkaTemplate).send(eq("currency-rate-created"), anyString(), eq(event));
    }

    @Test
    void sendCurrencyRateUpdated_ShouldSendToCorrectTopic() throws Exception {
        setupTopics();
        CurrencyRateEvent event = createTestEvent(CurrencyRateEvent.EventType.UPDATED);
        CompletableFuture<SendResult<String, CurrencyRateEvent>> future = CompletableFuture.completedFuture(null);

        when(kafkaTemplate.send(anyString(), anyString(), any(CurrencyRateEvent.class)))
                .thenReturn(future);

        currencyRateEventProducer.sendCurrencyRateUpdated(event);

        verify(kafkaTemplate).send(eq("currency-rate-updated"), anyString(), eq(event));
    }

    @Test
    void sendCurrencyRateDeleted_ShouldSendToCorrectTopic() throws Exception {
        setupTopics();
        CurrencyRateEvent event = createTestEvent(CurrencyRateEvent.EventType.DELETED);
        CompletableFuture<SendResult<String, CurrencyRateEvent>> future = CompletableFuture.completedFuture(null);

        when(kafkaTemplate.send(anyString(), anyString(), any(CurrencyRateEvent.class)))
                .thenReturn(future);

        currencyRateEventProducer.sendCurrencyRateDeleted(event);

        verify(kafkaTemplate).send(eq("currency-rate-deleted"), anyString(), eq(event));
    }


    @Test
    void sendEvent_WithDifferentEventTypes_ShouldUseCorrectKeyFormat() throws Exception {
        setupTopics();

        CurrencyRateEvent createdEvent = createTestEvent(CurrencyRateEvent.EventType.CREATED);
        createdEvent.setCurrencyCode("USD");
        createdEvent.setRateDate(LocalDate.of(2024, 1, 16));

        CurrencyRateEvent updatedEvent = createTestEvent(CurrencyRateEvent.EventType.UPDATED);
        updatedEvent.setCurrencyCode("EUR");
        updatedEvent.setRateDate(LocalDate.of(2024, 1, 17));

        CurrencyRateEvent deletedEvent = createTestEvent(CurrencyRateEvent.EventType.DELETED);
        deletedEvent.setCurrencyCode("GBP");
        deletedEvent.setRateDate(LocalDate.of(2024, 1, 18));

        CompletableFuture<SendResult<String, CurrencyRateEvent>> future = CompletableFuture.completedFuture(null);
        when(kafkaTemplate.send(anyString(), anyString(), any(CurrencyRateEvent.class)))
                .thenReturn(future);

        currencyRateEventProducer.sendCurrencyRateCreated(createdEvent);
        currencyRateEventProducer.sendCurrencyRateUpdated(updatedEvent);
        currencyRateEventProducer.sendCurrencyRateDeleted(deletedEvent);

        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        verify(kafkaTemplate, times(3)).send(anyString(), keyCaptor.capture(), any(CurrencyRateEvent.class));

        assertThat(keyCaptor.getAllValues()).containsExactly(
                "USD-2024-01-16",
                "EUR-2024-01-17",
                "GBP-2024-01-18"
        );
    }

    private CurrencyRateEvent createTestEvent(CurrencyRateEvent.EventType eventType) {
        return new CurrencyRateEvent(
                UUID.fromString("abb7b36d-39f3-455b-8741-8d034a74ceb7"),
                "USD",
                "US Dollar",
                new BigDecimal("75.5000"),
                LocalDate.of(2024, 1, 15),
                LocalDateTime.now(),
                LocalDateTime.now(),
                eventType,
                LocalDateTime.now()
        );
    }
}
