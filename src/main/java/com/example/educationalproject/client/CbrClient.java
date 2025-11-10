package com.example.educationalproject.client;

import com.example.educationalproject.model.cbr.ValCurs;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "cbr-client", url = "${cbr.url}")
public interface CbrClient {

    @GetMapping
    ValCurs getDailyRates(@RequestParam("date_req") String date);

}
