package com.example.price.convert;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CurrencyConverter {
    private static final BigDecimal USD_TO_KRW = BigDecimal.valueOf(1300.0); // 환율 값 (예시)

    public static BigDecimal convertToKRW(BigDecimal usdtPrice) {
        return usdtPrice.multiply(USD_TO_KRW).setScale(8, RoundingMode.HALF_UP);
    }
}
