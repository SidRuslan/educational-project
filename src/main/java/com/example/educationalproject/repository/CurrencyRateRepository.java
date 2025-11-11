package com.example.educationalproject.repository;

import com.example.educationalproject.entity.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, UUID> {
    Optional<CurrencyRate> findByCurrencyCodeAndRateDate(String currencyCode, LocalDate rateDate);

    Optional<CurrencyRate> findByCurrencyCode(String currencyCode);
    boolean existsByCurrencyCode(String currencyCode);

}

