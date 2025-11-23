package com.example.educationalproject.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRateEvent {

    private UUID id;
    private String currencyCode;
    private String currencyName;
    private BigDecimal exchangeRate;
    private LocalDate rateDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private EventType eventType;
    private LocalDateTime eventTimestamp;

    public enum EventType {
        CREATED, UPDATED, DELETED
    }

}
