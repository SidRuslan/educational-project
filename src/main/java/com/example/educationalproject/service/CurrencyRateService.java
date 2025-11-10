package com.example.educationalproject.service;

import com.example.educationalproject.dto.CurrencyRateRequest;
import com.example.educationalproject.dto.CurrencyRateResponse;

import java.util.List;
import java.util.UUID;

public interface CurrencyRateService {

    CurrencyRateResponse createCurrencyRate(CurrencyRateRequest request);

    CurrencyRateResponse getCurrencyRateById(UUID id);

    List<CurrencyRateResponse> getAllCurrencyRates();

    CurrencyRateResponse getCurrencyRateByCode(String currencyCode);

    CurrencyRateResponse updateCurrencyRate(UUID id, CurrencyRateRequest request);

    void deleteCurrencyRate(UUID id);
}
