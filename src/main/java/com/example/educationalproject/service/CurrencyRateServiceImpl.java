package com.example.educationalproject.service;

import com.example.educationalproject.dto.CurrencyRateRequest;
import com.example.educationalproject.dto.CurrencyRateResponse;
import com.example.educationalproject.entity.CurrencyRate;
import com.example.educationalproject.exception.CurrencyRateAlreadyExistsException;
import com.example.educationalproject.exception.CurrencyRateNotFoundException;
import com.example.educationalproject.mapper.CurrencyRateMapper;
import com.example.educationalproject.repository.CurrencyRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CurrencyRateServiceImpl implements CurrencyRateService {

    private final CurrencyRateRepository currencyRateRepository;
    private final CurrencyRateMapper currencyRateMapper;

    @Override
    public CurrencyRateResponse createCurrencyRate(CurrencyRateRequest request) {
        log.debug("Creating currency rate for {} on {}", request.getCurrencyCode(), request.getRateDate());

        if (currencyRateRepository.existsByCurrencyCodeAndRateDate(
                request.getCurrencyCode(), request.getRateDate())) {
            throw new CurrencyRateAlreadyExistsException(
                    String.format("Currency rate for %s on %s already exists",
                    request.getCurrencyCode(), request.getRateDate()));
        }

        CurrencyRate currencyRate = currencyRateMapper.toEntity(request);
        CurrencyRate newCurrencyRate = currencyRateRepository.save(currencyRate);

        log.info("Created currency rate with id: {} for {} on {}",
                newCurrencyRate.getId(), newCurrencyRate.getCurrencyCode(), newCurrencyRate.getRateDate());

        return currencyRateMapper.toResponse(newCurrencyRate);
    }

    @Transactional(readOnly = true)
    @Override
    public CurrencyRateResponse getCurrencyRateById(UUID id) {
        log.debug("Fetching currency rate by id: {}", id);

        CurrencyRate currencyRate = currencyRateRepository.findById(id)
                .orElseThrow(() -> new CurrencyRateNotFoundException(
                        String.format("Currency rate with id %s not found", id)));

        return currencyRateMapper.toResponse(currencyRate);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CurrencyRateResponse> getAllCurrencyRates() {
        log.debug("Fetching all currency rates");

        List<CurrencyRate> rates = currencyRateRepository.findAll();

        return currencyRateMapper.toResponseList(rates);
    }

    @Transactional(readOnly = true)
    @Override
    public CurrencyRateResponse getCurrencyRateByCode(String currencyCode) {
        log.debug("Fetching latest currency rate for {}", currencyCode);

        CurrencyRate currencyRate = currencyRateRepository
                .findByCurrencyCode(currencyCode)
                .orElseThrow(() -> new CurrencyRateNotFoundException(
                        String.format("No currency rate found for %s", currencyCode)));

        return currencyRateMapper.toResponse(currencyRate);
    }

    @Override
    public CurrencyRateResponse updateCurrencyRate(UUID id, CurrencyRateRequest request) {
        log.debug("Updating currency rate with id: {}", id);

        CurrencyRate existingRate = currencyRateRepository.findById(id)
                .orElseThrow(() -> new CurrencyRateNotFoundException(
                        String.format("Currency rate with id %s not found", id)));

        if (!existingRate.getCurrencyCode().equals(request.getCurrencyCode()) ||
        !existingRate.getRateDate().equals(request.getRateDate())) {
            if (currencyRateRepository.existsByCurrencyCodeAndRateDate(
                    request.getCurrencyCode(), request.getRateDate())) {
                throw new CurrencyRateAlreadyExistsException(
                        String.format("Currency rate for %s on %s already exists",
                                request.getCurrencyCode(), request.getRateDate()));
            }
        }

        currencyRateMapper.updateEntityFromRequest(request, existingRate);
        CurrencyRate updatedCurrencyRate =  currencyRateRepository.save(existingRate);

        log.info("Updated currency rate with id: {}", updatedCurrencyRate.getId());

        return currencyRateMapper.toResponse(updatedCurrencyRate);
    }

    @Override
    public void deleteCurrencyRate(UUID id) {
        log.debug("Deleting currency rate with id: {}", id);

        if (!currencyRateRepository.existsById(id)) {
            throw new CurrencyRateNotFoundException(
                    String.format("Currency rate with id %s not found", id));
        }

        currencyRateRepository.deleteById(id);
        log.info("Deleted currency rate with id: {}", id);
    }
}
