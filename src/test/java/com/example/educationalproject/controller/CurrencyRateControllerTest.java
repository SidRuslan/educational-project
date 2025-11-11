package com.example.educationalproject.controller;

import com.example.educationalproject.dto.CurrencyRateRequest;
import com.example.educationalproject.dto.CurrencyRateResponse;
import com.example.educationalproject.service.CurrencyRateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CurrencyRateController.class)
public class CurrencyRateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CurrencyRateService currencyRateService;

    @Test
    void shouldCreateCurrencyRate() throws Exception {
        CurrencyRateRequest request = CurrencyRateRequest.builder()
                .currencyCode("USD")
                .currencyName("US Dollar")
                .exchangeRate(new BigDecimal("75.5000"))
                .rateDate(LocalDate.of(2025, 11, 11))
                .build();

        CurrencyRateResponse response = CurrencyRateResponse.builder()
                .id(UUID.fromString("6cbabb71-f62d-4021-870b-864f36c52e2c"))
                .currencyCode("USD")
                .currencyName("US Dollar")
                .exchangeRate(new BigDecimal("75.5000"))
                .rateDate(LocalDate.of(2025, 11, 11))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(currencyRateService.createCurrencyRate(any(CurrencyRateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/currency-rates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("6cbabb71-f62d-4021-870b-864f36c52e2c"))
                .andExpect(jsonPath("$.currencyCode").value("USD"))
                .andExpect(jsonPath("$.exchangeRate").value(75.5000));
    }

    @Test
    void shouldReturnBadRequestWhenCreatingInvalidCurrencyRate() throws Exception {
        CurrencyRateRequest invalidRequest = CurrencyRateRequest.builder()
                .currencyCode("")
                .currencyName("US Dollar")
                .exchangeRate(new BigDecimal("-75.5000"))
                .rateDate(LocalDate.of(2025, 11, 11))
                .build();

        mockMvc.perform(post("/api/v1/currency-rates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetAllCurrencyRates() throws Exception {
        CurrencyRateResponse response1 = CurrencyRateResponse.builder()
                .id(UUID.fromString("abb7b36d-39f3-455b-8741-8d034a74ceb7"))
                .currencyCode("USD")
                .build();
        CurrencyRateResponse response2 = CurrencyRateResponse.builder()
                .id(UUID.fromString("6cbabb71-f62d-4021-870b-864f36c52e2c"))
                .currencyCode("EUR")
                .build();

        when(currencyRateService.getAllCurrencyRates()).thenReturn(List.of(response1, response2));

        mockMvc.perform(get("/api/v1/currency-rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].currencyCode").value("USD"))
                .andExpect(jsonPath("$[1].currencyCode").value("EUR"));
    }

    @Test
    void shouldGetCurrencyRateById() throws Exception {
        UUID id = UUID.fromString("abb7b36d-39f3-455b-8741-8d034a74ceb7");
        CurrencyRateResponse response = CurrencyRateResponse.builder()
                .id(id)
                .currencyCode("USD")
                .exchangeRate(new BigDecimal("75.5000"))
                .build();

        when(currencyRateService.getCurrencyRateById(id))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/currency-rates/abb7b36d-39f3-455b-8741-8d034a74ceb7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("abb7b36d-39f3-455b-8741-8d034a74ceb7"))
                .andExpect(jsonPath("$.currencyCode").value("USD"))
                .andExpect(jsonPath("$.exchangeRate").value(75.5000));
    }

    @Test
    void shouldUpdateCurrencyRate() throws Exception {
        UUID id = UUID.fromString("abb7b36d-39f3-455b-8741-8d034a74ceb7");
        CurrencyRateRequest request = CurrencyRateRequest.builder()
                .currencyCode("USD")
                .currencyName("US Dollar")
                .exchangeRate(new BigDecimal("75.5000"))
                .rateDate(LocalDate.of(2025, 11, 11))
                .build();

        CurrencyRateResponse response = CurrencyRateResponse.builder()
                .id(id)
                .currencyCode("USD")
                .exchangeRate(new BigDecimal("75.5000"))
                .build();

        when(currencyRateService.updateCurrencyRate(eq(id), any(CurrencyRateRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/currency-rates/abb7b36d-39f3-455b-8741-8d034a74ceb7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("abb7b36d-39f3-455b-8741-8d034a74ceb7"))
                .andExpect(jsonPath("$.exchangeRate").value(75.5000));
    }

    @Test
    void shouldDeleteCurrencyRate() throws Exception {
        mockMvc.perform(delete("/api/v1/currency-rates/abb7b36d-39f3-455b-8741-8d034a74ceb7"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldGetCurrencyRateByCode() throws Exception {
        CurrencyRateResponse response = CurrencyRateResponse.builder()
                .id(UUID.fromString("abb7b36d-39f3-455b-8741-8d034a74ceb7"))
                .currencyCode("USD")
                .exchangeRate(new BigDecimal("75.5000"))
                .build();

        when(currencyRateService.getCurrencyRateByCode("USD"))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/currency-rates/currency/USD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currencyCode").value("USD"))
                .andExpect(jsonPath("$.exchangeRate").value(75.5000));
    }
}
