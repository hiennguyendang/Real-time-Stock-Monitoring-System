package com.myproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockFeeder {
    private List<Stock> stockList = new ArrayList<>();
    private Map<String, List<StockViewer>> viewers = new HashMap<>();
    private static StockFeeder instance = null;

    private StockFeeder() {}

    public static StockFeeder getInstance() {
        if (instance == null) instance = new StockFeeder();
        return instance;
    }

    public void addStock(Stock stock) {
        Stock existingStock = stockList.stream().filter(s -> s.getCode().equals(stock.getCode())).findFirst().orElse(null);
        if (existingStock != null) {
            if ( !existingStock.getName().equals(stock.getName())) {
                stockList.remove(existingStock);
                stockList.add(stock);
            }
        }
        else stockList.add(stock);
    }

    public void registerViewer(String code, StockViewer stockViewer) {
        if (!stockList.stream().anyMatch(s -> s.getCode().equals(code))) {
            Logger.errorRegister(code);
            return;
        }
        List<StockViewer> stockViewers = viewers.computeIfAbsent(code, k -> new ArrayList<>());    
        for (StockViewer viewer : stockViewers) {
            if (viewer.getClass() == stockViewer.getClass()) {
                Logger.errorRegister(code);
                return;
            }
        }
        stockViewers.add(stockViewer);
    }

    public void unregisterViewer(String code, StockViewer stockViewer) {
        List<StockViewer> stockViewers = viewers.get(code);
        if (stockViewers == null || !stockViewers.remove(stockViewer))
            Logger.errorUnregister(code);
        else if (stockViewers.isEmpty()) 
            viewers.remove(code);
    }

    public void notify(StockPrice stockPrice) {
        List<StockViewer> stockViewers = viewers.get(stockPrice.getCode());
        if (stockViewers == null) return;
        for (StockViewer viewer : stockViewers) viewer.onUpdate(stockPrice);
    }
}
