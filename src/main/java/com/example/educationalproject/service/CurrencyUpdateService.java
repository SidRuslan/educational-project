package com.example.educationalproject.service;

import com.example.educationalproject.client.CbrClient;
import com.example.educationalproject.entity.CurrencyRate;
import com.example.educationalproject.model.cbr.ValCurs;
import com.example.educationalproject.model.cbr.Valute;
import com.example.educationalproject.repository.CurrencyRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyUpdateService {

    private static final DateTimeFormatter CBR_DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter CBR_RESPONSE_DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final CbrClient cbrClient;
    private final CurrencyRateRepository currencyRateRepository;

    @Scheduled(cron = "0 0 1 * * ?")
    public void fakeUpdateCurrencyRates() {
        log.info("Fake exchange rate update: started");

        try {
            Thread.sleep(2000);
            log.info("Fake exchange rate update: successfully completed");
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            log.error("Fake update interrupted", ex);
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void realUpdateCurrencyRates() {
        log.info("Real exchange rate update: started");

        try {
            String today = LocalDate.now().format(CBR_DATE_FORMAT);
            ValCurs valCurs = cbrClient.getDailyRates(today);

            if(valCurs == null || valCurs.getValutes() == null) {
                log.error("Unable to obtain data from the Central Bank");
                return;
            }

            LocalDate rateDate = LocalDate.parse(valCurs.getDate(), CBR_RESPONSE_DATE_FORMAT);
            List<Valute> valutes = valCurs.getValutes();

            int updatedCount = 0;
            for (Valute valute : valutes) {
                try {
                    updateCurrencyRate(valute, rateDate);
                    updatedCount++;
                } catch (Exception ex) {
                    log.error("Error updating currency exchange rate {}: {}",
                            valute.getCharCode(), ex.getMessage());
                }
            }

            log.info("Live exchange rate update: {} records successfully updated", updatedCount);


        } catch (Exception ex) {
            log.error("Error updating exchange rates from the Central Bank", ex);
        }
    }

    public void updateCurrencyRate(Valute valute, LocalDate rateDate) {

        String valueString = valute.getValue().replace(",", ".");
        BigDecimal exchangeRate = new BigDecimal(valueString)
                .divide(BigDecimal.valueOf(valute.getNominal()), 6, BigDecimal.ROUND_UP);

        currencyRateRepository.findByCurrencyCodeAndRateDate(valute.getCharCode(), rateDate)
                .ifPresentOrElse(existing -> {
                            existing.setExchangeRate(exchangeRate);
                            currencyRateRepository.save(existing);
                        }, () -> {
                            CurrencyRate newRate = CurrencyRate.builder()
                                    .currencyCode(valute.getCharCode())
                                    .currencyName(valute.getName())
                                    .exchangeRate(exchangeRate)
                                    .rateDate(rateDate)
                                    .build();

                            currencyRateRepository.save(newRate);
                        }
                );
    }


}
