package com.example.educationalproject.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyRateResponse {

    private UUID id;
    private String currencyCode;
    private String currencyName;
    private BigDecimal exchangeRate;
    private LocalDate rateDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}