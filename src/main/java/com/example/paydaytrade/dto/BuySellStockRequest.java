package com.example.paydaytrade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuySellStockRequest {
    private BigDecimal targetPrice;
    private Integer amount;
    private String symbol;
}
