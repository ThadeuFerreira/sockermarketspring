package com.stockmarket.stockmarketspring.controller;

import com.stockmarket.stockmarketspring.dto.StockDataSummaryDTO;
import com.stockmarket.stockmarketspring.model.StockData;
import com.stockmarket.stockmarketspring.service.AlphaAdvantageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/data")
public class DataController {

    private final AlphaAdvantageService alphaAdvantageService;

    @Autowired
    public DataController(AlphaAdvantageService alphaAdvantageService) {
        this.alphaAdvantageService = alphaAdvantageService;
    }

    @PostMapping("/db_create")
    public ResponseEntity<Map<String, StockDataSummaryDTO>> createDatabase() {

//        1. MSFT
//        2. AAPL
//        3. NVDA
//        4. AMZN
//        5. META
//        6. GOOGL
//        7. GOOG
//        8. BRK.B
//        9. LLY
//        10. AVGO


        LocalDate startDate = LocalDate.now().minusDays(5);

        //List<String> symbols = List.of("MSFT", "AAPL", "NVDA", "AMZN", "META", "GOOGL", "GOOG", "BRK.B", "LLY", "AVGO");
        List<String> symbols = List.of("IBM");

        Map<String, StockDataSummaryDTO> stringStockDataSummaryDTOHashMap = new HashMap<>();
        List<StockDataSummaryDTO> stockDataSummaryDTOList = new ArrayList<>();
        symbols.forEach(symbol -> {
            StockDataSummaryDTO sdto = alphaAdvantageService.fetchStockData(symbol, startDate);
                    stringStockDataSummaryDTOHashMap.put(symbol, sdto);
        }
        );

        return ResponseEntity.ok(stringStockDataSummaryDTOHashMap);
    }

    @PostMapping("/collect_data")
    public ResponseEntity<String> collectData() {

        return ResponseEntity.ok("Data collected and storage successfully.");
    }
}