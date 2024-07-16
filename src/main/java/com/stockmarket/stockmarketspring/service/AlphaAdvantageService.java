package com.stockmarket.stockmarketspring.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockmarket.stockmarketspring.dto.StockInsertUpdateInfoDTO;
import com.stockmarket.stockmarketspring.model.StockData;
import com.stockmarket.stockmarketspring.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AlphaAdvantageService {

    @Value("${alphavantage.api.key}")
    private String apiKey;

    private static final String BASE_URL = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&outputsize=full&symbol=%s&apikey=%s";

    private final RestTemplate restTemplate;

    private static StockRepository stockRepository;

    @Autowired
    public AlphaAdvantageService(RestTemplate restTemplate, StockRepository stockRepository) {
        this.restTemplate = restTemplate;
        this.stockRepository = stockRepository;
    }

    public StockInsertUpdateInfoDTO fetchStockData(String symbol, LocalDate startDate) {
        StockInsertUpdateInfoDTO stockInsertUpdateInfoDTO;
        String url = String.format(BASE_URL, symbol, apiKey);

        StockData lastStockData = stockRepository.findLastStockData();

        if (lastStockData == null || lastStockData.getDate().isBefore(startDate)) {
            stockInsertUpdateInfoDTO = getNewDataFromApi(symbol, url);
            return stockInsertUpdateInfoDTO;
        }
        long totalRows = stockRepository.count();
        return new StockInsertUpdateInfoDTO(0,totalRows);
    }

    private StockInsertUpdateInfoDTO getNewDataFromApi(String symbol, String url) {
        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {
                }
        );

        Map<String, Object> response = responseEntity.getBody();


        if (response == null || !response.containsKey("Time Series (Daily)") || !response.containsKey("Meta Data")) {
            loadFromSampleData(response);
        }
        if (response == null) {
            throw new RuntimeException("No data found for symbol: " + symbol);
        }

        extractedMetadata(response);


        @SuppressWarnings("unchecked")
        Map<String, Map<String, String>> timeSeries = (Map<String, Map<String, String>>) response.get("Time Series (Daily)");

        List<StockData> stockDataList = new ArrayList<>();

        timeSeries.forEach((date, dailyData) -> {
            StockData stockData = createStockData(symbol, LocalDate.parse(date), dailyData);
            stockDataList.add(stockData);
        });

        stockRepository.saveAll(stockDataList);
        long inserted = stockDataList.size();
        long totalRows = stockRepository.count();

        return new StockInsertUpdateInfoDTO(inserted, totalRows);

    }

    @Value("${json.file.path}")
    private String jsonFilePath;

    private void loadFromSampleData(Map<String, Object> response) {
        //load file sample_response.json
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(new ClassPathResource(jsonFilePath).getInputStream());
            //map jsonNode to response
            jsonNode.fields().forEachRemaining(entry -> {
                String key = entry.getKey();
                JsonNode value = entry.getValue();
                if (value.isObject()) {
                    Map<String, String> map = objectMapper.convertValue(value, Map.class);
                    response.put(key, map);
                } else {
                    response.put(key, value.asText());
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void extractedMetadata(Map<String, Object> response) {
        @SuppressWarnings("unchecked")
        Map<String, String> metaData = (Map<String, String>) response.get("Meta Data");

        metaData.forEach((key, value) -> System.out.println(key + " : " + value));
    }

    private StockData createStockData(String symbol, LocalDate date, Map<String, String> dailyData) {
        StockData stockData = new StockData();
        stockData.setSymbol(symbol);
        stockData.setDate(date);
        stockData.setOpenPrice(new BigDecimal(dailyData.get("1. open")));
        stockData.setHighPrice(new BigDecimal(dailyData.get("2. high")));
        stockData.setLowPrice(new BigDecimal(dailyData.get("3. low")));
        stockData.setClosePrice(new BigDecimal(dailyData.get("4. close")));
        stockData.setVolume(Long.parseLong(dailyData.get("5. volume")));
        return stockData;
    }
}