package com.example.educationalproject.controller;

import com.example.educationalproject.dto.CurrencyRateRequest;
import com.example.educationalproject.dto.CurrencyRateResponse;
import com.example.educationalproject.service.CurrencyRateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/currency-rates")
public class CurrencyRateController {

    private final CurrencyRateService currencyRateService;

    @PostMapping
    public ResponseEntity<CurrencyRateResponse> createCurrencyRate(@Valid @RequestBody CurrencyRateRequest request) {
        CurrencyRateResponse response = currencyRateService.createCurrencyRate(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CurrencyRateResponse>> getAllCurrencyRates() {
        List<CurrencyRateResponse> responses = currencyRateService.getAllCurrencyRates();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CurrencyRateResponse> getCurrencyRateById(@PathVariable UUID id) {
        CurrencyRateResponse response = currencyRateService.getCurrencyRateById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/currency/{currencyCode}")
    public ResponseEntity<CurrencyRateResponse> getCurrencyRateByCurrencyCode(@PathVariable String currencyCode) {
        CurrencyRateResponse response = currencyRateService.getCurrencyRateByCode(currencyCode);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}")
    public ResponseEntity<CurrencyRateResponse> updateCurrencyRate(@PathVariable UUID id,
                                                                   @Valid @RequestBody CurrencyRateRequest request) {
        CurrencyRateResponse response = currencyRateService.updateCurrencyRate(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurrencyRate(@PathVariable UUID id) {
        currencyRateService.deleteCurrencyRate(id);
        return ResponseEntity.noContent().build();
    }

}
