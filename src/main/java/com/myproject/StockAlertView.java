package com.myproject;

import java.util.HashMap;
import java.util.Map;

public class StockAlertView implements StockViewer {
    private double alertThresholdHigh;
    private double alertThresholdLow;
    private Map<String, Double> lastAlertedPrices = new HashMap<>();

    public StockAlertView(double highThreshold, double lowThreshold) {
        this.alertThresholdHigh = highThreshold;
        this.alertThresholdLow = lowThreshold;
    }

    @Override
    public void onUpdate(StockPrice stockPrice) {
        String stockCode = stockPrice.getCode();
        double currentPrice = stockPrice.getAvgPrice();
        Double lastAlertedPrice = lastAlertedPrices.get(stockCode);
        if (currentPrice >= alertThresholdHigh && (lastAlertedPrice == null || currentPrice != lastAlertedPrice))
            alertAbove(stockCode, currentPrice);
        else if (currentPrice <= alertThresholdLow && (lastAlertedPrice == null || currentPrice != lastAlertedPrice))
            alertBelow(stockCode, currentPrice);
        else if (currentPrice > alertThresholdLow && currentPrice < alertThresholdHigh)
            lastAlertedPrices.remove(stockCode);
    }

    private void alertAbove(String stockCode, double price) {
        Logger.logAlert(stockCode, price);
        lastAlertedPrices.put(stockCode, price);
    }

    private void alertBelow(String stockCode, double price) {
        Logger.logAlert(stockCode, price);
        lastAlertedPrices.put(stockCode, price);

    }
}
