package com.example.paydaytrade.controller;

import com.example.paydaytrade.dto.BuySellStockRequest;
import com.example.paydaytrade.resource.Ticker;
import com.example.paydaytrade.service.StockService;
import com.example.paydaytrade.wrapper.StockWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/stocks")
public class StockController {

    private final StockService service;

    @PostMapping
    BigDecimal home() {
        return service.findStock("TSLA").getStock().getQuote().getPrice();
    }

    @PostMapping("/all")
    List<Ticker> findAll() {
        List<String> stocks = new ArrayList<>();
        stocks.add("TSLA");
        stocks.add("GOOG");
        stocks.add("AMZN");
        stocks.add("ABNB");
        stocks.add("ADBE");
        stocks.add("AAPL");
        return service.findStocks(stocks);
    }

    @PostMapping("/buy")
    public String buyStock(@RequestBody @Valid BuySellStockRequest buySellStockRequest){
        return service.buyStock(buySellStockRequest);
    }

    @PostMapping("/sell")
    public String sellStock(@RequestBody @Valid BuySellStockRequest buySellStockRequest){
        return service.sellStock(buySellStockRequest);
    }
}
