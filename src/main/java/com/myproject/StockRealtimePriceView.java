package com.myproject;

import java.util.HashMap;
import java.util.Map;

public class StockRealtimePriceView implements StockViewer {
    private final Map<String, Double> lastPrices = new HashMap<>();

    @Override
    public void onUpdate(StockPrice stockPrice) {
        String stockCode = stockPrice.getCode();
        double currentPrice = stockPrice.getAvgPrice();
        Double lastPrice = lastPrices.get(stockCode);
        if (lastPrice != null && currentPrice != lastPrice)
            Logger.logRealtime(stockCode, currentPrice);
        lastPrices.put(stockCode, currentPrice);
    }
}
