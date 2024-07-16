package com.stockmarket.stockmarketspring.dto;

import java.util.List;


public record CollectDataRequestPayload(List<String> symbols, String start_date) {
}