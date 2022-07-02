package com.example.paydaytrade.service;

import com.example.paydaytrade.dto.BuySellStockRequest;
import com.example.paydaytrade.resource.Ticker;
import com.example.paydaytrade.wrapper.StockWrapper;

import java.util.List;

public interface StockService {

    StockWrapper findStock(String symbol);

    List<Ticker> findStocks(List<String> symbol);

    String sellStock(BuySellStockRequest buySellStockRequest);

    String buyStock(BuySellStockRequest buySellStockRequest);
}
