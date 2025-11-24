package com.example.educationalproject.integration;

import com.example.educationalproject.dto.CurrencyRateRequest;
import com.example.educationalproject.dto.CurrencyRateResponse;
import com.example.educationalproject.repository.CurrencyRateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CurrencyRateIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    CurrencyRateRepository currencyRateRepository;

    @Test
    void shouldCreateAndRetrieveCurrencyRate() {
        CurrencyRateRequest request = CurrencyRateRequest.builder()
                .currencyCode("USD")
                .currencyName("US Dollar")
                .exchangeRate(new BigDecimal("75.5000"))
                .rateDate(LocalDate.of(2025, 11, 11))
                .build();

        ResponseEntity<CurrencyRateResponse> createResponse = restTemplate
                .postForEntity( "/api/v1/currency-rates", request, CurrencyRateResponse.class);

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getBody()).isNotNull();
        assertThat(createResponse.getBody().getCurrencyCode()).isEqualTo("USD");

        UUID createdId = createResponse.getBody().getId();

        ResponseEntity<CurrencyRateResponse> getResponse = restTemplate
                .getForEntity("/api/v1/currency-rates/" + createdId, CurrencyRateResponse.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().getCurrencyCode()).isEqualTo("USD");
        assertThat(getResponse.getBody().getExchangeRate()).isEqualTo(new BigDecimal("75.5000"));
    }

    @Test
    void shouldReturnNotFoundForNonExistentId() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/v1/currency-rates/6cbabb71-f62d-4021-870b-864f36c52e2c", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
