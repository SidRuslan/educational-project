package com.example.educationalproject.service;

import com.example.educationalproject.client.CbrClient;
import com.example.educationalproject.entity.CurrencyRate;
import com.example.educationalproject.model.cbr.ValCurs;
import com.example.educationalproject.model.cbr.Valute;
import com.example.educationalproject.repository.CurrencyRateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurrencyUpdateServiceTest {

    @Mock
    private CbrClient cbrClient;

    @Mock
    private CurrencyRateRepository currencyRateRepository;

    @InjectMocks
    private CurrencyUpdateService currencyUpdateService;

    @Test
    void fakeUpdateCurrencyRates_ShouldLogAndSleep() {
        long startTime = System.currentTimeMillis();

        currencyUpdateService.fakeUpdateCurrencyRates();

        long endTime = System.currentTimeMillis();
        assertThat(endTime - startTime).isGreaterThanOrEqualTo(2000);
    }

    @Test
    void realUpdateCurrencyRates_ShouldUpdateRatesSuccessfully() {
        String today = "11/11/2025";

        ValCurs valCurs = new ValCurs();
        valCurs.setDate("11.11.2025");

        Valute usdValute = new Valute();
        usdValute.setCharCode("USD");
        usdValute.setName("Доллар США");
        usdValute.setNominal(1);
        usdValute.setValue("75,5000");

        Valute eurValute = new Valute();
        eurValute.setCharCode("EUR");
        eurValute.setName("Евро");
        eurValute.setNominal(1);
        eurValute.setValue("85,3000");

        valCurs.setValutes(List.of(usdValute, eurValute));

        when(cbrClient.getDailyRates(today)).thenReturn(valCurs);
        when(currencyRateRepository.findByCurrencyCodeAndRateDate(anyString(), any(LocalDate.class)))
                .thenReturn(Optional.empty());

        currencyUpdateService.realUpdateCurrencyRates();

        verify(cbrClient).getDailyRates(today);
        verify(currencyRateRepository, times(2)).save(any(CurrencyRate.class));
    }

    @Test
    void realUpdateCurrencyRates_WhenCbrReturnsNull_ShouldLogError() {
        when(cbrClient.getDailyRates(anyString())).thenReturn(null);

        currencyUpdateService.realUpdateCurrencyRates();

        verify(cbrClient).getDailyRates(anyString());
        verify(currencyRateRepository, never()).save(any(CurrencyRate.class));
    }

    @Test
    void realUpdateCurrencyRates_WhenValutesIsNull_ShouldLogError() {
        ValCurs valCurs = new ValCurs();
        valCurs.setDate("11.11.2025");
        valCurs.setValutes(null);

        when(cbrClient.getDailyRates(anyString())).thenReturn(valCurs);

        currencyUpdateService.realUpdateCurrencyRates();

        verify(cbrClient).getDailyRates(anyString());
        verify(currencyRateRepository, never()).save(any(CurrencyRate.class));
    }

    @Test
    void realUpdateCurrencyRates_WhenUpdateFailsForOneCurrency_ShouldContinueWithOthers() {
        String today = "11/11/2025";
        LocalDate rateDate = LocalDate.of(2025, 11, 11);

        ValCurs valCurs = new ValCurs();
        valCurs.setDate("11.11.2025");

        Valute usdValute = Valute.builder()
                .charCode("USD")
                .name("Доллар США")
                .nominal(1)
                .value("75,5000")
                .build();

        Valute invalidValute = Valute.builder()
                .charCode("INVALID")
                .name("Invalid Currency")
                .nominal(0)
                .value("100,0000")
                .build();


        valCurs.setValutes(List.of(usdValute, invalidValute));

        when(cbrClient.getDailyRates(today)).thenReturn(valCurs);
        when(currencyRateRepository.findByCurrencyCodeAndRateDate("USD", rateDate))
                .thenReturn(Optional.empty());

        currencyUpdateService.realUpdateCurrencyRates();

        verify(cbrClient).getDailyRates(today);
        verify(currencyRateRepository, times(1)).save(any(CurrencyRate.class));
    }

    @Test
    void updateCurrencyRate_WhenCurrencyNotExists_ShouldCreateNewRate() {
        Valute valute = Valute.builder()
                .charCode("USD")
                .name("Доллар США")
                .nominal(1)
                .value("75,5000")
                .build();

        LocalDate rateDate = LocalDate.of(2025, 11, 11);

        when(currencyRateRepository.findByCurrencyCodeAndRateDate("USD", rateDate))
                .thenReturn(Optional.empty());

        currencyUpdateService.updateCurrencyRate(valute, rateDate);

        ArgumentCaptor<CurrencyRate> currencyRateCaptor = ArgumentCaptor.forClass(CurrencyRate.class);
        verify(currencyRateRepository).save(currencyRateCaptor.capture());

        CurrencyRate savedRate = currencyRateCaptor.getValue();
        assertThat(savedRate.getCurrencyCode()).isEqualTo("USD");
        assertThat(savedRate.getCurrencyName()).isEqualTo("Доллар США");
        assertThat(savedRate.getExchangeRate()).isEqualTo(new BigDecimal("75.500000"));
        assertThat(savedRate.getRateDate()).isEqualTo(rateDate);
    }

    @Test
    void updateCurrencyRate_WhenCurrencyExists_ShouldUpdateExistingRate() {
        Valute valute = Valute.builder()
                .charCode("EUR")
                .name("Евро")
                .nominal(1)
                .value("85,3000")
                .build();

        LocalDate rateDate = LocalDate.of(2025, 11, 11);

        CurrencyRate existingRate = CurrencyRate.builder()
                .id(UUID.fromString("6cbabb71-f62d-4021-870b-864f36c52e2c"))
                .currencyCode("EUR")
                .currencyName("Евро")
                .exchangeRate(new BigDecimal("80.0000"))
                .rateDate(rateDate)
                .build();

        when(currencyRateRepository.findByCurrencyCodeAndRateDate("EUR", rateDate))
                .thenReturn(Optional.of(existingRate));

        currencyUpdateService.updateCurrencyRate(valute, rateDate);

        verify(currencyRateRepository).save(existingRate);
        assertThat(existingRate.getExchangeRate()).isEqualTo(new BigDecimal("85.300000"));
    }

    @Test
    void updateCurrencyRate_WithNominalGreaterThanOne_ShouldCalculateCorrectRate() {
        Valute valute = Valute.builder()
                .charCode("JPY")
                .name("Японская йена")
                .nominal(100)
                .value("65,1234")
                .build();

        LocalDate rateDate = LocalDate.of(2025, 11, 11);

        when(currencyRateRepository.findByCurrencyCodeAndRateDate("JPY", rateDate))
                .thenReturn(Optional.empty());

        currencyUpdateService.updateCurrencyRate(valute, rateDate);

        ArgumentCaptor<CurrencyRate> currencyRateCaptor = ArgumentCaptor.forClass(CurrencyRate.class);
        verify(currencyRateRepository).save(currencyRateCaptor.capture());

        CurrencyRate savedRate = currencyRateCaptor.getValue();
        assertThat(savedRate.getExchangeRate()).isEqualTo(new BigDecimal("0.651234"));
    }

    @Test
    void updateCurrencyRate_WithCommaInValue_ShouldReplaceWithDot() {
        Valute valute = Valute.builder()
                .charCode("USD")
                .name("Доллар США")
                .nominal(1)
                .value("75,5000")
                .build();
        LocalDate rateDate = LocalDate.of(2025, 11, 11);

        when(currencyRateRepository.findByCurrencyCodeAndRateDate("USD", rateDate))
                .thenReturn(Optional.empty());

        currencyUpdateService.updateCurrencyRate(valute, rateDate);

        ArgumentCaptor<CurrencyRate> currencyRateCaptor = ArgumentCaptor.forClass(CurrencyRate.class);
        verify(currencyRateRepository).save(currencyRateCaptor.capture());

        CurrencyRate savedRate = currencyRateCaptor.getValue();
        assertThat(savedRate.getExchangeRate()).isEqualTo(new BigDecimal("75.500000"));
    }

    @Test
    void updateCurrencyRate_WhenRepositoryThrowsException_ShouldPropagateException() {
        Valute valute = Valute.builder()
                .charCode("USD")
                .name("Доллар США")
                .nominal(1)
                .value("75,5000")
                .build();
        LocalDate rateDate = LocalDate.of(2025, 11, 11);

        when(currencyRateRepository.findByCurrencyCodeAndRateDate("USD", rateDate))
                .thenThrow(new RuntimeException("Database error"));

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class,
                () -> currencyUpdateService.updateCurrencyRate(valute, rateDate));
    }

}
