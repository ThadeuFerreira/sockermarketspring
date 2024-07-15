package com.stockmarket.stockmarketspring.dto;

public class StockDataSummaryDTO {
    private long rowsInserted;
    private long totalRows;

    public StockDataSummaryDTO(long rowsInserted, long totalRows) {
        this.rowsInserted = rowsInserted;
        this.totalRows = totalRows;
    }

    public long getRowsInserted() {
        return rowsInserted;
    }

    public void setRowsInserted(long rowsInserted) {
        this.rowsInserted = rowsInserted;
    }

    public long getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(long totalRows) {
        this.totalRows = totalRows;
    }

    @Override
    public String toString() {
        return "StockDataSummaryDTO{" +
                "rowsInserted=" + rowsInserted +
                ", totalRows=" + totalRows +
                '}';
    }
}