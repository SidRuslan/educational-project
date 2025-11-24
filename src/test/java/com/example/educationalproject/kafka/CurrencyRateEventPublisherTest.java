package com.example.educationalproject.kafka;

import com.example.educationalproject.dto.CurrencyRateResponse;
import com.example.educationalproject.dto.kafka.CurrencyRateEvent;
import com.example.educationalproject.mapper.CurrencyRateEventMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurrencyRateEventPublisherTest {

    @Mock
    private CurrencyRateEventProducer currencyRateEventProducer;

    @Mock
    private CurrencyRateEventMapper currencyRateEventMapper;

    @InjectMocks
    private CurrencyRateEventPublisher currencyRateEventPublisher;

    @Test
    void publishCurrencyRateCreated_ShouldCallMapperAndProducer() {
        CurrencyRateResponse response = createTestResponse();
        CurrencyRateEvent event = createTestEvent(CurrencyRateEvent.EventType.CREATED);

        when(currencyRateEventMapper.toCreateEvent(response)).thenReturn(event);

        currencyRateEventPublisher.publishCurrencyRateCreated(response);

        verify(currencyRateEventMapper).toCreateEvent(response);
        verify(currencyRateEventProducer).sendCurrencyRateCreated(event);
    }

    @Test
    void publishCurrencyRateUpdated_ShouldCallMapperAndProducer() {
        CurrencyRateResponse response = createTestResponse();
        CurrencyRateEvent event = createTestEvent(CurrencyRateEvent.EventType.UPDATED);

        when(currencyRateEventMapper.toUpdatedEvent(response)).thenReturn(event);

        currencyRateEventPublisher.publishCurrencyRateUpdated(response);

        verify(currencyRateEventMapper).toUpdatedEvent(response);
        verify(currencyRateEventProducer).sendCurrencyRateUpdated(event);
    }

    @Test
    void publishCurrencyRateDeleted_ShouldCallMapperAndProducer() {
        CurrencyRateResponse response = createTestResponse();
        CurrencyRateEvent event = createTestEvent(CurrencyRateEvent.EventType.DELETED);

        when(currencyRateEventMapper.toDeletedEvent(response)).thenReturn(event);

        currencyRateEventPublisher.publishCurrencyRateDeleted(response);

        verify(currencyRateEventMapper).toDeletedEvent(response);
        verify(currencyRateEventProducer).sendCurrencyRateDeleted(event);
    }

    @Test
    void publishCurrencyRateCreated_WhenMapperThrowsException_ShouldLogErrorAndNotPropagate() {
        CurrencyRateResponse response = createTestResponse();

        when(currencyRateEventMapper.toCreateEvent(response))
                .thenThrow(new RuntimeException("Mapper error"));

        currencyRateEventPublisher.publishCurrencyRateCreated(response);

        verify(currencyRateEventMapper).toCreateEvent(response);
        verify(currencyRateEventProducer, never()).sendCurrencyRateCreated(any());
    }

    @Test
    void publishCurrencyRateCreated_WhenProducerThrowsException_ShouldLogErrorAndNotPropagate() {
        CurrencyRateResponse response = createTestResponse();
        CurrencyRateEvent event = createTestEvent(CurrencyRateEvent.EventType.CREATED);

        when(currencyRateEventMapper.toCreateEvent(response)).thenReturn(event);
        doThrow(new RuntimeException("Producer error"))
                .when(currencyRateEventProducer).sendCurrencyRateCreated(event);

        currencyRateEventPublisher.publishCurrencyRateCreated(response);

        verify(currencyRateEventMapper).toCreateEvent(response);
        verify(currencyRateEventProducer).sendCurrencyRateCreated(event);
    }

    @Test
    void publishCurrencyRateUpdated_WhenMapperThrowsException_ShouldLogErrorAndNotPropagate() {
        CurrencyRateResponse response = createTestResponse();

        when(currencyRateEventMapper.toUpdatedEvent(response))
                .thenThrow(new RuntimeException("Mapper error"));

        currencyRateEventPublisher.publishCurrencyRateUpdated(response);

        verify(currencyRateEventMapper).toUpdatedEvent(response);
        verify(currencyRateEventProducer, never()).sendCurrencyRateUpdated(any());
    }

    @Test
    void publishCurrencyRateUpdated_WhenProducerThrowsException_ShouldLogErrorAndNotPropagate() {
        CurrencyRateResponse response = createTestResponse();
        CurrencyRateEvent event = createTestEvent(CurrencyRateEvent.EventType.UPDATED);

        when(currencyRateEventMapper.toUpdatedEvent(response)).thenReturn(event);
        doThrow(new RuntimeException("Producer error"))
                .when(currencyRateEventProducer).sendCurrencyRateUpdated(event);

        currencyRateEventPublisher.publishCurrencyRateUpdated(response);

        verify(currencyRateEventMapper).toUpdatedEvent(response);
        verify(currencyRateEventProducer).sendCurrencyRateUpdated(event);
    }

    @Test
    void publishCurrencyRateDeleted_WhenMapperThrowsException_ShouldLogErrorAndNotPropagate() {
        CurrencyRateResponse response = createTestResponse();

        when(currencyRateEventMapper.toDeletedEvent(response))
                .thenThrow(new RuntimeException("Mapper error"));

        currencyRateEventPublisher.publishCurrencyRateDeleted(response);

        verify(currencyRateEventMapper).toDeletedEvent(response);
        verify(currencyRateEventProducer, never()).sendCurrencyRateDeleted(any());
    }

    @Test
    void publishCurrencyRateDeleted_WhenProducerThrowsException_ShouldLogErrorAndNotPropagate() {
        CurrencyRateResponse response = createTestResponse();
        CurrencyRateEvent event = createTestEvent(CurrencyRateEvent.EventType.DELETED);

        when(currencyRateEventMapper.toDeletedEvent(response)).thenReturn(event);
        doThrow(new RuntimeException("Producer error"))
                .when(currencyRateEventProducer).sendCurrencyRateDeleted(event);

        currencyRateEventPublisher.publishCurrencyRateDeleted(response);

        verify(currencyRateEventMapper).toDeletedEvent(response);
        verify(currencyRateEventProducer).sendCurrencyRateDeleted(event);
    }

    @Test
    void publishCurrencyRateCreated_ShouldUseCorrectEventType() {
        CurrencyRateResponse response = createTestResponse();
        CurrencyRateEvent createdEvent = createTestEvent(CurrencyRateEvent.EventType.CREATED);

        when(currencyRateEventMapper.toCreateEvent(response)).thenReturn(createdEvent);

        currencyRateEventPublisher.publishCurrencyRateCreated(response);

        verify(currencyRateEventProducer).sendCurrencyRateCreated(createdEvent);
        assertThat(createdEvent.getEventType()).isEqualTo(CurrencyRateEvent.EventType.CREATED);
    }

    @Test
    void publishCurrencyRateUpdated_ShouldUseCorrectEventType() {
        CurrencyRateResponse response = createTestResponse();
        CurrencyRateEvent updatedEvent = createTestEvent(CurrencyRateEvent.EventType.UPDATED);

        when(currencyRateEventMapper.toUpdatedEvent(response)).thenReturn(updatedEvent);

        currencyRateEventPublisher.publishCurrencyRateUpdated(response);

        verify(currencyRateEventProducer).sendCurrencyRateUpdated(updatedEvent);
        assertThat(updatedEvent.getEventType()).isEqualTo(CurrencyRateEvent.EventType.UPDATED);
    }

    @Test
    void publishCurrencyRateDeleted_ShouldUseCorrectEventType() {
        CurrencyRateResponse response = createTestResponse();
        CurrencyRateEvent deletedEvent = createTestEvent(CurrencyRateEvent.EventType.DELETED);

        when(currencyRateEventMapper.toDeletedEvent(response)).thenReturn(deletedEvent);

        currencyRateEventPublisher.publishCurrencyRateDeleted(response);

        verify(currencyRateEventProducer).sendCurrencyRateDeleted(deletedEvent);
        assertThat(deletedEvent.getEventType()).isEqualTo(CurrencyRateEvent.EventType.DELETED);
    }



    private CurrencyRateResponse createTestResponse() {
        return new CurrencyRateResponse(
                UUID.fromString("abb7b36d-39f3-455b-8741-8d034a74ceb7"),
                "USD",
                "US Dollar",
                new BigDecimal("75.5000"),
                LocalDate.of(2025, 11, 22),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
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
