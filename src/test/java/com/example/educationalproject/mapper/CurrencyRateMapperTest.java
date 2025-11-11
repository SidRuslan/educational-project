package com.example.educationalproject.mapper;

import com.example.educationalproject.dto.CurrencyRateRequest;
import com.example.educationalproject.dto.CurrencyRateResponse;
import com.example.educationalproject.entity.CurrencyRate;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class CurrencyRateMapperTest {

    private final CurrencyRateMapper mapper = Mappers.getMapper(CurrencyRateMapper.class);

    @Test
    void shouldMapRequestToEntity() {
        CurrencyRateRequest request = new CurrencyRateRequest();
        request.setCurrencyCode("USD");
        request.setCurrencyName("US Dollar");
        request.setExchangeRate(new BigDecimal("75.5000"));
        request.setRateDate(LocalDate.of(2025, 11, 11));

        CurrencyRate entity = mapper.toEntity(request);

        assertThat(entity.getCurrencyCode()).isEqualTo("USD");
        assertThat(entity.getCurrencyName()).isEqualTo("US Dollar");
        assertThat(entity.getExchangeRate()).isEqualTo(new BigDecimal("75.5000"));
        assertThat(entity.getRateDate()).isEqualTo(LocalDate.of(2025, 11, 11));
        assertThat(entity.getId()).isNull();
        assertThat(entity.getCreatedAt()).isNull();
        assertThat(entity.getUpdatedAt()).isNull();
    }

    @Test
    void shouldMapEntityToResponse() {
        CurrencyRate entity = new CurrencyRate();
        entity.setId(UUID.fromString("6cbabb71-f62d-4021-870b-864f36c52e2c"));
        entity.setCurrencyCode("EUR");
        entity.setCurrencyName("Euro");
        entity.setExchangeRate(new BigDecimal("85.3000"));
        entity.setRateDate(LocalDate.of(2025, 11, 11));
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        CurrencyRateResponse response = mapper.toResponse(entity);

        assertThat(response.getId()).isEqualTo(UUID.fromString("6cbabb71-f62d-4021-870b-864f36c52e2c"));
        assertThat(response.getCurrencyCode()).isEqualTo("EUR");
        assertThat(response.getCurrencyName()).isEqualTo("Euro");
        assertThat(response.getExchangeRate()).isEqualTo(new BigDecimal("85.3000"));
        assertThat(response.getRateDate()).isEqualTo(LocalDate.of(2025, 11, 11));
        assertThat(response.getCreatedAt()).isNotNull();
        assertThat(response.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldMapEntityListToResponseList() {
        CurrencyRate entity1 = new CurrencyRate();
        entity1.setId(UUID.fromString("6cbabb71-f62d-4021-870b-864f36c52e2c"));
        entity1.setCurrencyCode("USD");

        CurrencyRate entity2 = new CurrencyRate();
        entity2.setId(UUID.fromString("abb7b36d-39f3-455b-8741-8d034a74ceb7"));
        entity2.setCurrencyCode("EUR");

        List<CurrencyRate> entities = List.of(entity1, entity2);

        List<CurrencyRateResponse> responses = mapper.toResponseList(entities);

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getCurrencyCode()).isEqualTo("USD");
        assertThat(responses.get(1).getCurrencyCode()).isEqualTo("EUR");
    }

    @Test
    void shouldUpdateEntityFromRequest() {
        CurrencyRate existingEntity = new CurrencyRate();
        existingEntity.setId(UUID.fromString("abb7b36d-39f3-455b-8741-8d034a74ceb7"));
        existingEntity.setCurrencyCode("OLD");
        existingEntity.setCurrencyName("Old Name");
        existingEntity.setExchangeRate(new BigDecimal("50.0000"));
        existingEntity.setRateDate(LocalDate.of(2023, 12, 1));
        existingEntity.setCreatedAt(LocalDateTime.now().minusDays(1));
        existingEntity.setUpdatedAt(LocalDateTime.now().minusDays(1));

        CurrencyRateRequest request = new CurrencyRateRequest();
        request.setCurrencyCode("USD");
        request.setCurrencyName("US Dollar");
        request.setExchangeRate(new BigDecimal("75.5000"));
        request.setRateDate(LocalDate.of(2024, 1, 15));

        mapper.updateEntityFromRequest(request, existingEntity);

        assertThat(existingEntity.getId()).isEqualTo(UUID.fromString("abb7b36d-39f3-455b-8741-8d034a74ceb7"));
        assertThat(existingEntity.getCurrencyCode()).isEqualTo("USD");
        assertThat(existingEntity.getCurrencyName()).isEqualTo("US Dollar");
        assertThat(existingEntity.getExchangeRate()).isEqualTo(new BigDecimal("75.5000"));
        assertThat(existingEntity.getRateDate()).isEqualTo(LocalDate.of(2024, 1, 15));
        assertThat(existingEntity.getCreatedAt()).isNotNull();
        assertThat(existingEntity.getUpdatedAt()).isNotNull();
    }

}
