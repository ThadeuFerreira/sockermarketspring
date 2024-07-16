package com.stockmarket.stockmarketspring.service;

import com.stockmarket.stockmarketspring.model.StockData;
import com.stockmarket.stockmarketspring.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DataService {

    private final StockRepository stockRepository;

    @Autowired
    public DataService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public StockData getStockData(String symbol, LocalDate date) {
        // get stock data from repository
        return stockRepository.findBySymbolAndDate(symbol, date);
    }
}
