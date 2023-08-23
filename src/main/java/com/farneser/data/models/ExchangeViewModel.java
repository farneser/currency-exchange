package com.farneser.data.models;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class ExchangeViewModel extends ExchangeRate {
    @SerializedName("amount")
    private final BigDecimal _amount;
    @SerializedName("convertedAmount")
    private final BigDecimal _convertedAmount;
    public ExchangeViewModel(Currency baseCurrency, Currency targetCurrency, BigDecimal amount, BigDecimal convertedAmount) {
        super(baseCurrency, targetCurrency, BigDecimal.valueOf(amount.doubleValue()/convertedAmount.doubleValue()));
        _convertedAmount = convertedAmount;
        _amount = amount;
    }
}
