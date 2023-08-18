package com.farneser.data.models;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class ExchangeRate extends BaseEntity {
    @SerializedName("baseCurrency")
    private int _baseCurrencyId;
    @SerializedName("targetCurrency")
    private int _targetCurrencyId;
    @SerializedName("rate")
    private BigInteger _rate;

    public int getBaseCurrencyId() {
        return _baseCurrencyId;
    }

    public void setBaseCurrencyId(int baseCurrencyId) {
        _baseCurrencyId = baseCurrencyId;
    }

    public int getTargetCurrencyId() {
        return _targetCurrencyId;
    }

    public void setTargetCurrencyId(int targetCurrencyId) {
        _targetCurrencyId = targetCurrencyId;
    }

    public BigInteger getRate() {
        return _rate;
    }

    public void setRate(BigInteger rate) {
        _rate = rate;
    }
}
