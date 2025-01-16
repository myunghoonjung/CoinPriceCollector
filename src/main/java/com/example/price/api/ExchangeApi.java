package com.example.price.api;

import java.math.BigDecimal;

public interface ExchangeApi {
    BigDecimal getPrice(String symbol); // BTC 가격 반환
}
