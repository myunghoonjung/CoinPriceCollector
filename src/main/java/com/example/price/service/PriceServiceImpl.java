package com.example.price.service;

import java.math.BigDecimal;
import java.util.List;

import com.example.price.api.ExchangeApi;

public class PriceServiceImpl {
    private final List<ExchangeApi> exchangeApis;

    public PriceServiceImpl(List<ExchangeApi> exchangeApis, int threadPoolSize) {
        this.exchangeApis = exchangeApis;
    }

    public BigDecimal getPriceFromExchange(String symbol, Class<? extends ExchangeApi> exchangeApiClass) {
        for (ExchangeApi exchange : exchangeApis) {
            if (exchangeApiClass.isInstance(exchange)) {
                return exchange.getPrice(symbol);
            }
        }
        return BigDecimal.ZERO;
    }

}

