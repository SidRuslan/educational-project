package com.example.educationalproject.integration;

import com.example.educationalproject.client.CbrClient;
import com.example.educationalproject.entity.CurrencyRate;
import com.example.educationalproject.model.cbr.ValCurs;
import com.example.educationalproject.model.cbr.Valute;
import com.example.educationalproject.repository.CurrencyRateRepository;
import com.example.educationalproject.service.CurrencyUpdateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class CurrencyUpdateServiceIntegrationTest {

    @Autowired
    private CurrencyUpdateService currencyUpdateService;

    @Autowired
    private CurrencyRateRepository currencyRateRepository;

    @MockBean
    private CbrClient cbrClient;

    @Test
    void realUpdateCurrencyRates_ShouldSaveDataToDatabase() {
        LocalDate rateDate = LocalDate.of(2025,  11, 11);

        ValCurs valCurs = new ValCurs();
        valCurs.setDate("11.11.2025");

        Valute usdValute = Valute.builder()
                .charCode("USD")
                .name("Доллар США")
                .nominal(1)
                .value("75,5000")
                .build();

        Valute eurValute = Valute.builder()
                .charCode("EUR")
                .name("Евро")
                .nominal(1)
                .value("85,3000")
                .build();


        valCurs.setValutes(List.of(usdValute, eurValute));

        when(cbrClient.getDailyRates(anyString())).thenReturn(valCurs);

        currencyUpdateService.realUpdateCurrencyRates();

        Optional<CurrencyRate> usdRate = currencyRateRepository.findByCurrencyCodeAndRateDate("USD", rateDate);
        Optional<CurrencyRate> eurRate = currencyRateRepository.findByCurrencyCodeAndRateDate("EUR", rateDate);

        assertThat(usdRate).isPresent();
        assertThat(eurRate).isPresent();

        assertThat(usdRate.get().getExchangeRate()).isEqualTo(new BigDecimal("75.5000"));
        assertThat(eurRate.get().getExchangeRate()).isEqualTo(new BigDecimal("85.3000"));
    }

    @Test
    void updateCurrencyRate_ShouldUpdateExistingRecord() {
        LocalDate rateDate = LocalDate.of(2025,  11, 11);

        CurrencyRate existingRate = CurrencyRate.builder()
                .currencyCode("GBP")
                .currencyName("Фунт стерлингов")
                .exchangeRate(new BigDecimal("95.0000"))
                .rateDate(rateDate)
                .build();

        currencyRateRepository.save(existingRate);

        Valute valute = new Valute();
        valute.setCharCode("GBP");
        valute.setName("Фунт стерлингов");
        valute.setNominal(1);
        valute.setValue("96,5000");

        currencyUpdateService.updateCurrencyRate(valute, rateDate);

        Optional<CurrencyRate> updatedRate = currencyRateRepository.findByCurrencyCodeAndRateDate("GBP", rateDate);
        assertThat(updatedRate).isPresent();
        assertThat(updatedRate.get().getExchangeRate()).isEqualTo(new BigDecimal("96.5000"));
    }


}
