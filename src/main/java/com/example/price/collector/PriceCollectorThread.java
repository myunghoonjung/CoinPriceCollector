package com.example.price.collector;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import com.example.price.api.BinanceApi;
import com.example.price.api.BithumbApi;
import com.example.price.api.ExchangeApi;
import com.example.price.convert.CurrencyConverter;
import com.example.price.service.PriceServiceImpl;

public class PriceCollectorThread implements Runnable {
    private final PriceServiceImpl priceService;

    public PriceCollectorThread() {
        // 거래소 API 구현체 등록
        List<ExchangeApi> apis = Arrays.asList(new BithumbApi(), new BinanceApi());
        this.priceService = new PriceServiceImpl(apis, 2); // 2개의 스레드로 병렬 처리
    }

    @Override
    public void run() {
        while (true) {
            try {
                // BTC와 ETH 가격 수집
                BigDecimal bithumbBtcPrice = priceService.getPriceFromExchange("BTC", BithumbApi.class);
                BigDecimal binanceBtcPrice = priceService.getPriceFromExchange("BTC", BinanceApi.class);
                BigDecimal binanceBtcPriceInKRW = CurrencyConverter.convertToKRW(binanceBtcPrice);

                System.out.println("BTC 가격 (빗썸, KRW): " + bithumbBtcPrice.toPlainString());
                System.out.println("BTC 가격 (바이낸스, USDT): " + binanceBtcPrice.toPlainString());
                System.out.println("BTC 가격 (바이낸스, KRW): " + binanceBtcPriceInKRW.toPlainString());

                BigDecimal bithumbEthPrice = priceService.getPriceFromExchange("ETH", BithumbApi.class);
                BigDecimal binanceEthPrice = priceService.getPriceFromExchange("ETH", BinanceApi.class);
                BigDecimal binanceEthPriceInKRW = CurrencyConverter.convertToKRW(binanceEthPrice);

                System.out.println("ETH 가격 (빗썸, KRW): " + bithumbEthPrice.toPlainString());
                System.out.println("ETH 가격 (바이낸스, USDT): " + binanceEthPrice.toPlainString());
                System.out.println("ETH 가격 (바이낸스, KRW): " + binanceEthPriceInKRW.toPlainString());

                // 10초 간격으로 수집
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                System.err.println("Price collection interrupted: " + e.getMessage());
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
