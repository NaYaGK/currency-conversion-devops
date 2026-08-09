package com.vamshi.currencyconversion;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name = "currency-exchange", url = "localhost:8000")
@FeignClient(name = "currency-exchange")
public interface CurrencyExchangeFeign {


    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    CurrencyConversion
    currencyExchange(@PathVariable("from") String from, @PathVariable("to") String to);
}
