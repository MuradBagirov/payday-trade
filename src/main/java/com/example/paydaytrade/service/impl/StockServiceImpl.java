package com.example.paydaytrade.service.impl;

import com.example.paydaytrade.config.UserContext;
import com.example.paydaytrade.dto.BuySellStockRequest;
import com.example.paydaytrade.enums.Status;
import com.example.paydaytrade.model.Stock;
import com.example.paydaytrade.model.User;
import com.example.paydaytrade.repository.StockRepository;
import com.example.paydaytrade.resource.Ticker;
import com.example.paydaytrade.resource.TickerQuote;
import com.example.paydaytrade.service.StockService;
import com.example.paydaytrade.wrapper.StockWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yahoofinance.YahooFinance;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {
    private final StockRepository stockRepository;
    public StockWrapper findStock(String symbol) {
        try {
            return new StockWrapper(YahooFinance.get(symbol));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public List<Ticker> findStocks(final List<String> tickers) {
        List<Ticker> tickerList = new ArrayList<>();
        Ticker ticker;
        TickerQuote tickerQuote;
        List<StockWrapper> stockWrappers = tickers
                .stream()
                .map(this::findStock)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        for (StockWrapper stockWrapper1 : stockWrappers) {
            ticker = new Ticker();
            tickerQuote = new TickerQuote();
            ticker.setQuote(tickerQuote);

            ticker.getQuote().setPrice(stockWrapper1.getStock().getQuote().getPrice());
            ticker.setName(stockWrapper1.getStock().getName());
            ticker.setSymbol(stockWrapper1.getStock().getSymbol());
            tickerList.add(ticker);
        }


        return tickerList;
    }

    @Override
    public String buyStock(BuySellStockRequest buySellStockRequest){
        User user = UserContext.getUser();
        StockWrapper stocks = findStock(buySellStockRequest.getSymbol());

        if (stocks == null){
            throw new RuntimeException("stock not found");
        }
        Stock stock = Stock.builder()
                .targetPrice(buySellStockRequest.getTargetPrice())
                .amount(buySellStockRequest.getAmount())
                .user(user)
                .status(Status.WAITING_FOR_BUY)
                .symbol(buySellStockRequest.getSymbol())
                .build();

        stockRepository.save(stock);
        return "You will be notified via email when process is done!";
    }

    @Override
    public String sellStock(BuySellStockRequest buySellStockRequest){
        User user = UserContext.getUser();
        StockWrapper stocks = findStock(buySellStockRequest.getSymbol());

        if (stocks == null){
            throw new RuntimeException("stock not found");
        }
        Stock existingStock = stockRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Stock information not found!"));

        existingStock.setStatus(Status.WAITING_FOR_SELL);
        existingStock.setTargetPrice(buySellStockRequest.getTargetPrice());

        stockRepository.save(existingStock);
        return "You will be notified via email when process is done!";
    }
}
