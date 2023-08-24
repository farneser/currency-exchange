package com.farneser.data.models;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExchangeViewModel extends ExchangeRate {
    @SerializedName("amount")
    private final BigDecimal _amount;
    @SerializedName("convertedAmount")
    private final BigDecimal _convertedAmount;

    public ExchangeViewModel(Currency baseCurrency, Currency targetCurrency, BigDecimal amount, BigDecimal convertedAmount) {
        super(baseCurrency, targetCurrency, convertedAmount.divide(amount, RoundingMode.HALF_DOWN));
        _convertedAmount = convertedAmount;
        _amount = amount;
    }
}
