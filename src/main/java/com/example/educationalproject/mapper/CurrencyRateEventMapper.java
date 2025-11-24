package com.example.educationalproject.mapper;

import com.example.educationalproject.dto.CurrencyRateResponse;
import com.example.educationalproject.dto.kafka.CurrencyRateEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class, CurrencyRateEvent.EventType.class})
public interface CurrencyRateEventMapper {

    @Mapping(target = "eventType", expression = "java(EventType.CREATED)")
    @Mapping(target = "eventTimestamp", expression = "java(LocalDateTime.now())")
    CurrencyRateEvent toCreateEvent(CurrencyRateResponse response);

    @Mapping(target = "eventType", expression = "java(EventType.UPDATED)")
    @Mapping(target = "eventTimestamp", expression = "java(LocalDateTime.now())")
    CurrencyRateEvent toUpdatedEvent(CurrencyRateResponse response);

    @Mapping(target = "eventType", expression = "java(EventType.DELETED)")
    @Mapping(target = "eventTimestamp", expression = "java(LocalDateTime.now())")
    CurrencyRateEvent toDeletedEvent(CurrencyRateResponse response);

}
