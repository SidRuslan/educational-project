package com.example.educationalproject.mapper;

import com.example.educationalproject.dto.CurrencyRateResponse;
import com.example.educationalproject.dto.kafka.CurrencyRateEvent;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class CurrencyRateEventMapperTest {

    private final CurrencyRateEventMapper mapper = Mappers.getMapper(CurrencyRateEventMapper.class);

    @Test
    void toCreatedEvent_ShouldMapAllFieldsAndSetEventType() {
        CurrencyRateResponse response = createTestResponse();

        CurrencyRateEvent event = mapper.toCreateEvent(response);

        assertThat(event.getId()).isEqualTo(response.getId());
        assertThat(event.getCurrencyCode()).isEqualTo(response.getCurrencyCode());
        assertThat(event.getExchangeRate()).isEqualTo(response.getExchangeRate());
        assertThat(event.getEventType()).isEqualTo(CurrencyRateEvent.EventType.CREATED);
        assertThat(event.getEventTimestamp()).isNotNull();
    }

    @Test
    void toUpdatedEvent_ShouldSetCorrectEventType() {
        CurrencyRateResponse response = createTestResponse();

        CurrencyRateEvent event = mapper.toUpdatedEvent(response);

        assertThat(event.getEventType()).isEqualTo(CurrencyRateEvent.EventType.UPDATED);
        assertThat(event.getEventTimestamp()).isNotNull();
    }

    @Test
    void toDeletedEvent_FromResponse_ShouldSetCorrectEventType() {
        CurrencyRateResponse response = createTestResponse();

        CurrencyRateEvent event = mapper.toDeletedEvent(response);

        assertThat(event.getEventType()).isEqualTo(CurrencyRateEvent.EventType.DELETED);
        assertThat(event.getId()).isEqualTo(response.getId());
        assertThat(event.getCurrencyCode()).isEqualTo(response.getCurrencyCode());
        assertThat(event.getEventTimestamp()).isNotNull();
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
}
