package com.stockmarket.stockmarketspring.controller;

import com.stockmarket.stockmarketspring.dto.CollectDataRequestPayload;
import com.stockmarket.stockmarketspring.dto.StockInsertUpdateInfoDTO;
import com.stockmarket.stockmarketspring.service.AlphaAdvantageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class DataController {

    private final AlphaAdvantageService alphaAdvantageService;

    @Autowired
    public DataController(AlphaAdvantageService alphaAdvantageService) {
        this.alphaAdvantageService = alphaAdvantageService;
    }

    @PostMapping("/collect_data")
    public ResponseEntity<Map<String, StockInsertUpdateInfoDTO>> createDatabase(@RequestBody CollectDataRequestPayload requestPayload) {

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
        if (requestPayload.start_date() != null) {
            startDate = LocalDate.parse(requestPayload.start_date());
        }

        //List<String> symbols = List.of("MSFT", "AAPL", "NVDA", "AMZN", "META", "GOOGL", "GOOG", "BRK.B", "LLY", "AVGO");
        List<String> symbols = List.of("IBM");
        if (requestPayload.symbols() != null) {
            symbols = requestPayload.symbols();
        }

        Map<String, StockInsertUpdateInfoDTO> stringStockDataSummaryDTOHashMap = new HashMap<>();
        List<StockInsertUpdateInfoDTO> stockInsertUpdateInfoDTOList = new ArrayList<>();
        LocalDate finalStartDate = startDate;
        symbols.forEach(symbol -> {
            StockInsertUpdateInfoDTO sdto = alphaAdvantageService.fetchStockData(symbol, finalStartDate);
                    stringStockDataSummaryDTOHashMap.put(symbol, sdto);
        }
        );

        return ResponseEntity.ok(stringStockDataSummaryDTOHashMap);
    }

    @PostMapping("/db_create")
    public ResponseEntity<String> collectData() {

        return ResponseEntity.ok("Data collected and storage successfully.");
    }
}