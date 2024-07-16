package com.stockmarket.stockmarketspring.repository;

import com.stockmarket.stockmarketspring.model.StockData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<StockData, Long> {
    List<StockData> findBySymbolAndDateBetween(String symbol, LocalDate startDate, LocalDate endDate);
    StockData findBySymbolAndDate(String symbol, LocalDate date);

    @Query("SELECT s FROM StockData s WHERE s.symbol = ?1 ORDER BY s.date DESC LIMIT 1")
    StockData findLastStockData(String symbol);

}