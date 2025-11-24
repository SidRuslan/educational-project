package com.example.educationalproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurrencyRateRequest {

    @NotBlank(message = "Currency code is required")
    private String currencyCode;

    @NotBlank(message = "Currency name is required")
    private String currencyName;

    @NotNull(message = "Exchange rate is required")
    @Positive(message = "Exchange rate must be positive")
    private BigDecimal exchangeRate;

    @NotNull(message = "Rate date is required")
    private LocalDate rateDate;

}
