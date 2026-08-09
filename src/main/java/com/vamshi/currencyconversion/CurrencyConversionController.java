package com.vamshi.currencyconversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CurrencyConversionController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    CurrencyExchangeFeign currencyExchangeFeign;

    @GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion
    currencyConversion(@PathVariable("from") String from,
                       @PathVariable("to") String to,
                       @PathVariable("quantity") BigDecimal quantity) {

        Map<String, String> variables = new HashMap<>();
        variables.put("from", from);
        variables.put("to", to);
        CurrencyConversion cur = restTemplate
                .getForObject("http://localhost:8000/currency-exchange/from/{from}/to/{to}",
                        CurrencyConversion.class, variables);
        cur.setQuantity(quantity);
        cur.setInstance(cur.getInstance() + " Rest-template");
        cur.setTotalCalculatedAmount(quantity.multiply(cur.getConversionMultiple()));
        return cur;
    }

    @GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion currencyConversionFeign(@PathVariable("from") String from,
                            @PathVariable("to") String to,
                            @PathVariable("quantity") BigDecimal quantity) {
        CurrencyConversion currencyConversionFeign =
                currencyExchangeFeign.currencyExchange(from, to);
        currencyConversionFeign.setQuantity(quantity);
        currencyConversionFeign.setTotalCalculatedAmount
                (quantity.multiply(currencyConversionFeign.getConversionMultiple()));
        return currencyConversionFeign;
    }
}
