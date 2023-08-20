package com.farneser.data.models;

import com.google.gson.annotations.SerializedName;

public class ExchangeRate extends BaseEntity {
    @SerializedName("baseCurrency")
    private Currency _baseCurrency;
    private transient int _baseCurrencyId;
    @SerializedName("targetCurrency")
    private Currency _targetCurrency;
    private transient int _targetCurrencyId;
    @SerializedName("rate")
    private double _rate;

    public ExchangeRate(String baseCurrencyCode, String targetCurrencyCode, double rate) {
        _baseCurrency = new Currency(baseCurrencyCode, "", "");
        _targetCurrency = new Currency(targetCurrencyCode, "", "");
        _rate = rate;
    }

    public ExchangeRate(int baseCurrencyId, int targetCurrencyId, double rate) {
        _baseCurrencyId = baseCurrencyId;
        _targetCurrencyId = targetCurrencyId;
        _rate = rate;
    }

    public ExchangeRate(int id, int baseCurrencyId, int targetCurrencyId, double rate) {
        this(baseCurrencyId, targetCurrencyId, rate);
        _id = id;
    }

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

    public double getRate() {
        return _rate;
    }

    public void setRate(double rate) {
        _rate = rate;
    }

    public Currency getBaseCurrency() {
        return _baseCurrency;
    }

    public void setBaseCurrency(Currency currency) {
        _baseCurrencyId = currency.getId();
        _baseCurrency = currency;
    }

    public Currency getTargetCurrency() {
        return _targetCurrency;
    }

    public void setTargetCurrency(Currency currency) {
        _targetCurrencyId = currency.getId();
        _targetCurrency = currency;
    }
}
