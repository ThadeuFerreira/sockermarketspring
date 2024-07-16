package com.stockmarket.stockmarketspring.controller;

import com.stockmarket.stockmarketspring.dto.StockInfoDTO;
import com.stockmarket.stockmarketspring.model.StockData;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.stockmarket.stockmarketspring.service.DataService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class StockController {

    private final DataService dataService;

    @Autowired
    public StockController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/stock_data/{symbol}/{start_date}")
    public StockInfoDTO getStockData(@PathVariable String symbol, @PathVariable String start_date) {
        LocalDate startDate = checkDate(start_date);
        StockData stockData = dataService.getStockData(symbol, startDate);
        if (stockData == null) {
            return null;
        }
        return new StockInfoDTO(
                stockData.getClosePrice(),
                stockData.getOpenPrice(),
                stockData.getHighPrice(),
                stockData.getLowPrice(),
                stockData.getVolume());
    }

    static LocalDate checkDate(String sdate) {
        try {
            var date = LocalDate.parse(sdate);
            //check if date is in the future
            if (date.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Date cannot be in the future");
            }
            //check if date is on a weekend
            if (date.getDayOfWeek().getValue() > 5) {
                throw new IllegalArgumentException("Date cannot be on a weekend");
            }
            //TODO: check if date is a public holiday
            return date;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date: " + e.getMessage());
        }

    }
}
