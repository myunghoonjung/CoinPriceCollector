package com.example.price.manager;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PriceCollectorManager {
    private final ExecutorService executor;

    public PriceCollectorManager(int threadPoolSize) {
        this.executor = Executors.newFixedThreadPool(threadPoolSize);
    }

    public List<BigDecimal> collectPrices(List<Callable<BigDecimal>> tasks) {
        try {
            List<Future<BigDecimal>> futures = executor.invokeAll(tasks);
            return futures.stream()
                          .map(future -> {
                              try {
                                  return future.get();
                              } catch (Exception e) {
                                  e.printStackTrace();
                                  return BigDecimal.ZERO;
                              }
                          })
                          .toList();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Price collection interrupted", e);
        }
    }

    public void shutdown() {
        executor.shutdown();
    }
}
