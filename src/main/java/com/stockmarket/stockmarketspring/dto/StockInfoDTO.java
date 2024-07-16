package com.stockmarket.stockmarketspring.dto;

import java.math.BigDecimal;

public record StockInfoDTO (
    BigDecimal open_price,
    BigDecimal close_price,
    BigDecimal high_price,
    BigDecimal low_price,
    Long volume
) {
}


