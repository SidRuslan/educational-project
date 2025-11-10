package com.example.educationalproject.mapper;

import com.example.educationalproject.dto.CurrencyRateRequest;
import com.example.educationalproject.dto.CurrencyRateResponse;
import com.example.educationalproject.entity.CurrencyRate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CurrencyRateMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    CurrencyRate toEntity(CurrencyRateRequest request);

    CurrencyRateResponse toResponse(CurrencyRate entity);

    List<CurrencyRateResponse> toResponseList(List<CurrencyRate> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    CurrencyRate updateEntityFromRequest(CurrencyRateRequest request,
                                         @MappingTarget CurrencyRate entity);

}
