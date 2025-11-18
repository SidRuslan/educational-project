package com.example.educationalproject.repository;

import com.example.educationalproject.entity.CurrencyRate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class CurrencyRateRepositoryTest {

    @Autowired
    private CurrencyRateRepository currencyRateRepository;

    @Test
    void shouldSaveAndFindCurrencyRate() {
        CurrencyRate rate = CurrencyRate.builder()
                .currencyCode("USD")
                .currencyName("US Dollar")
                .exchangeRate(new BigDecimal("75.5000"))
                .rateDate(LocalDate.of(2025, 11, 11))
                .build();

        CurrencyRate saved = currencyRateRepository.save(rate);
        Optional<CurrencyRate> found = currencyRateRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getCurrencyCode()).isEqualTo("USD");
        assertThat(found.get().getExchangeRate()).isEqualTo(new BigDecimal("75.5000"));
    }

    @Test
    void shouldFindByCurrencyCodeAndRateDate() {
        LocalDate date = LocalDate.of(2025, 11, 11);
        CurrencyRate rate = CurrencyRate.builder()
                .currencyCode("EUR")
                .currencyName("Euro")
                .exchangeRate(new BigDecimal("85.3000"))
                .rateDate(date)
                .build();
        currencyRateRepository.save(rate);

        Optional<CurrencyRate> found = currencyRateRepository.findByCurrencyCodeAndRateDate("EUR", date);

        assertThat(found).isPresent();
        assertThat(found.get().getCurrencyCode()).isEqualTo("EUR");
        assertThat(found.get().getRateDate()).isEqualTo(date);
    }

    @Test
    void shouldFindByCurrencyCode() {
        LocalDate date = LocalDate.of(2025, 11, 11);
        CurrencyRate rate = CurrencyRate.builder()
                .currencyCode("EUR")
                .currencyName("Euro")
                .exchangeRate(new BigDecimal("85.3000"))
                .rateDate(date)
                .build();
        currencyRateRepository.save(rate);

        Optional<CurrencyRate> found = currencyRateRepository.findByCurrencyCode("EUR");

        assertThat(found).isPresent();
        assertThat(found.get().getCurrencyCode()).isEqualTo("EUR");
        assertThat(found.get().getExchangeRate()).isEqualTo(new BigDecimal("85.3000"));
    }

    @Test
    void shouldCheckExistenceByCurrencyCodeAndDate() {
        LocalDate date = LocalDate.of(2025, 11, 11);
        CurrencyRate rate = CurrencyRate.builder()
                .currencyCode("EUR")
                .currencyName("Euro")
                .exchangeRate(new BigDecimal("85.3000"))
                .rateDate(date)
                .build();
        currencyRateRepository.save(rate);

        boolean exist = currencyRateRepository.existsByCurrencyCodeAndRateDate("EUR", date);
        boolean notExist = currencyRateRepository.existsByCurrencyCodeAndRateDate("USD", date);

        assertThat(exist).isTrue();
        assertThat(notExist).isFalse();
    }
}
