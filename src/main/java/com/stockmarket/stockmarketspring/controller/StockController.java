package com.stockmarket.stockmarketspring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockController {

    @GetMapping("/stock_data/{symbol}/{start_date}")
    public String getStockData(String symbol, String start_date) {
        return "Stock data for " + symbol + " from " + start_date;
    }
}
