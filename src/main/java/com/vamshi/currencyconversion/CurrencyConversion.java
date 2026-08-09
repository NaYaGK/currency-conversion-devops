package com.vamshi.currencyconversion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Data
@NoArgsConstructor
public class CurrencyConversion {

    private String from;
    private String to;
    private BigDecimal conversionMultiple;
    private BigDecimal quantity;
    private int id;
    private BigDecimal totalCalculatedAmount;
    private String instance;

    public CurrencyConversion(int id, String from, String to, BigDecimal conversionMultiple, BigDecimal quantity) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.conversionMultiple = conversionMultiple;
        this.quantity = quantity;
    }
}
