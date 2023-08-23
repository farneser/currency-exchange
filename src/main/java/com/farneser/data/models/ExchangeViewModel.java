package com.farneser.data.models;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class ExchangeViewModel extends ExchangeRate {
    @SerializedName("amount")
    private final BigDecimal _amount;
    @SerializedName("convertedAmount")
    private final BigDecimal _convertedAmount;

    public ExchangeViewModel(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate, BigDecimal amount, BigDecimal convertedAmount) {
        super(baseCurrencyCode, targetCurrencyCode, rate);
        _convertedAmount = convertedAmount;
        _amount = amount;
    }
}
