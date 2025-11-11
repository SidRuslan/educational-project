package com.example.educationalproject.service;

import com.example.educationalproject.dto.CurrencyRateRequest;
import com.example.educationalproject.dto.CurrencyRateResponse;
import com.example.educationalproject.entity.CurrencyRate;
import com.example.educationalproject.exception.CurrencyRateAlreadyExistsException;
import com.example.educationalproject.exception.CurrencyRateNotFoundException;
import com.example.educationalproject.mapper.CurrencyRateMapper;
import com.example.educationalproject.mapper.CurrencyRateMapperImpl;
import com.example.educationalproject.repository.CurrencyRateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurrencyRateServiceTest {

    @Mock
    private CurrencyRateRepository currencyRateRepository;

    @Spy
    private CurrencyRateMapper currencyRateMapper = new CurrencyRateMapperImpl();

    @InjectMocks
    private CurrencyRateServiceImpl currencyRateService;

    @Test
    void shouldCreateCurrencyRateSuccessfully() {
        CurrencyRateRequest request = CurrencyRateRequest.builder()
                .currencyCode("USD")
                .currencyName("US Dollar")
                .exchangeRate(new BigDecimal("75.5000"))
                .rateDate(LocalDate.of(2025, 11, 11))
                .build();

        CurrencyRate savedEntity = CurrencyRate.builder()
                .id(UUID.fromString("abb7b36d-39f3-455b-8741-8d034a74ceb7"))
                .currencyCode("USD")
                .currencyName("US Dollar")
                .exchangeRate(new BigDecimal("75.5000"))
                .rateDate(LocalDate.of(2025, 11, 11))
                .build();

        when(currencyRateRepository.existsByCurrencyCode("USD")).thenReturn(false);
        when(currencyRateRepository.save(any(CurrencyRate.class))).thenReturn(savedEntity);

        CurrencyRateResponse response = currencyRateService.createCurrencyRate(request);

        assertThat(response.getId()).isEqualTo(UUID.fromString("abb7b36d-39f3-455b-8741-8d034a74ceb7"));
        assertThat(response.getCurrencyCode()).isEqualTo("USD");
        verify(currencyRateRepository).save(any(CurrencyRate.class));
    }

    @Test
    void shouldThrowExceptionWhenCreatingDuplicateCurrencyRate()  {
        CurrencyRateRequest request = CurrencyRateRequest.builder()
                .currencyCode("USD")
                .currencyName("US Dollar")
                .exchangeRate(new BigDecimal("75.5000"))
                .rateDate(LocalDate.of(2025, 11, 11))
                .build();

        when(currencyRateRepository.existsByCurrencyCode("USD")).thenReturn(true);

        assertThatThrownBy(() -> currencyRateService.createCurrencyRate(request))
                .isInstanceOf(CurrencyRateAlreadyExistsException.class)
                .hasMessage("Currency rate for USD already exists");
    }

    @Test
    void shouldGetCurrencyRateById() {
        UUID id = UUID.fromString("abb7b36d-39f3-455b-8741-8d034a74ceb7");
        CurrencyRate entity = CurrencyRate.builder()
                .id(id)
                .currencyCode("USD")
                .currencyName("US Dollar")
                .exchangeRate(new BigDecimal("75.5000"))
                .rateDate(LocalDate.of(2025, 11, 11))
                .build();

        when(currencyRateRepository.findById(id))
                .thenReturn(Optional.of(entity));

        CurrencyRateResponse response = currencyRateService
                .getCurrencyRateById(id);


        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getCurrencyCode()).isEqualTo("USD");
        assertThat(response.getExchangeRate()).isEqualTo(new BigDecimal("75.5000"));
    }

    @Test
    void shouldThrowExceptionWhenCurrencyRateNotFoundById() {
        UUID id = UUID.fromString("abb7b36d-39f3-455b-8741-8d034a74ceb7");
        when(currencyRateRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> currencyRateService.getCurrencyRateById(id))
                .isInstanceOf(CurrencyRateNotFoundException.class)
                .hasMessage("Currency rate with id abb7b36d-39f3-455b-8741-8d034a74ceb7 not found");
    }

    @Test
    void shouldGetAllCurrencyRates() {
        CurrencyRate rate1 = CurrencyRate.builder()
                .id(UUID.fromString("abb7b36d-39f3-455b-8741-8d034a74ceb7"))
                .currencyCode("USD")
                .build();
        CurrencyRate rate2 = CurrencyRate.builder()
                .id(UUID.fromString("6cbabb71-f62d-4021-870b-864f36c52e2c"))
                .currencyCode("EUR")
                .build();

        when(currencyRateRepository.findAll()).thenReturn(List.of(rate1, rate2));

        List<CurrencyRateResponse> responses = currencyRateService.getAllCurrencyRates();

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getCurrencyCode()).isEqualTo("USD");
        assertThat(responses.get(1).getCurrencyCode()).isEqualTo("EUR");
    }

    @Test
    void shouldUpdateCurrencyRateSuccessfully() {
        UUID id = UUID.fromString("6cbabb71-f62d-4021-870b-864f36c52e2c");

        CurrencyRate existingEntity = CurrencyRate.builder()
                .id(id)
                .currencyCode("OLD")
                .currencyName("Old Name")
                .exchangeRate(new BigDecimal("50.0000"))
                .rateDate(LocalDate.of(2025, 11, 11))
                .build();

        CurrencyRateRequest request = CurrencyRateRequest.builder()
                .currencyCode("USD")
                .currencyName("US Dollar")
                .exchangeRate(new BigDecimal("75.5000"))
                .rateDate(LocalDate.of(2025, 11, 15))
                .build();

        CurrencyRate updatedEntity = CurrencyRate.builder()
                .id(id)
                .currencyCode("USD")
                .currencyName("US Dollar")
                .exchangeRate(new BigDecimal("75.5000"))
                .rateDate(LocalDate.of(2025, 11, 15))
                .build();

        when(currencyRateRepository.findById(id))
                .thenReturn(Optional.of(existingEntity));
        when(currencyRateRepository.existsByCurrencyCode("USD")).thenReturn(false);
        when(currencyRateRepository.save(any(CurrencyRate.class))).thenReturn(updatedEntity);

        CurrencyRateResponse response = currencyRateService.updateCurrencyRate(id, request);

        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getCurrencyCode()).isEqualTo("USD");
        verify(currencyRateRepository).save(any(CurrencyRate.class));
    }

    @Test
    void shouldDeleteCurrencyRate() {
        UUID id = UUID.fromString("6cbabb71-f62d-4021-870b-864f36c52e2c");

        when(currencyRateRepository.existsById(id)).thenReturn(true);

        currencyRateService.deleteCurrencyRate(id);

        verify(currencyRateRepository).deleteById(id);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentCurrencyRate() {
        UUID id = UUID.fromString("6cbabb71-f62d-4021-870b-864f36c52e2c");

        when(currencyRateRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> currencyRateService.deleteCurrencyRate(id))
                .isInstanceOf(CurrencyRateNotFoundException.class)
                .hasMessage("Currency rate with id 6cbabb71-f62d-4021-870b-864f36c52e2c not found");

        verify(currencyRateRepository, never()).deleteById(any(UUID.class));
    }

}
