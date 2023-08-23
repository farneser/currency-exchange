package com.farneser.data.models;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.Objects;

public class ExchangeRate extends BaseEntity {
    @SerializedName("baseCurrency")
    private Currency _baseCurrency;
    @SerializedName("targetCurrency")
    private Currency _targetCurrency;
    @SerializedName("rate")
    private BigDecimal _rate;

    public ExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        _baseCurrency = new Currency(baseCurrencyCode, "", "");
        _targetCurrency = new Currency(targetCurrencyCode, "", "");
        _rate = rate;
    }

    public ExchangeRate(int baseCurrencyId, int targetCurrencyId, BigDecimal rate) {
        _baseCurrency = new Currency(baseCurrencyId, "", "", "");
        _targetCurrency = new Currency(targetCurrencyId, "", "", "");
        _rate = rate;
    }

    public ExchangeRate(int id, int baseCurrencyId, int targetCurrencyId, BigDecimal rate) {
        this(baseCurrencyId, targetCurrencyId, rate);
        _id = id;
    }

    public int getBaseCurrencyId() {

        if (_baseCurrency == null) {
            return 0;
        }

        return _baseCurrency._id;
    }

    public ExchangeRate(Currency baseCurrency, Currency targetCurrency, BigDecimal rate) {
        _baseCurrency = baseCurrency;
        _targetCurrency = targetCurrency;
        _rate = rate;
    }

    public void setBaseCurrencyId(int baseCurrencyId) {
        if (_baseCurrency == null) {
            _baseCurrency = new Currency("", "", "");
        }
        _baseCurrency.setId(baseCurrencyId);
    }

    public int getTargetCurrencyId() {
        if (_targetCurrency == null) {
            return 0;
        }

        return _targetCurrency.getId();
    }

    public void setTargetCurrencyId(int targetCurrencyId) {
        if (_targetCurrency == null) {
            _baseCurrency = new Currency("", "", "");
        }
        _targetCurrency.setId(targetCurrencyId);
    }

    public BigDecimal getRate() {
        return _rate;
    }

    public void setRate(BigDecimal rate) {
        _rate = rate;
    }

    public Currency getBaseCurrency() {
        return _baseCurrency;
    }

    public void setBaseCurrency(Currency currency) {
        _baseCurrency = currency;
    }

    public Currency getTargetCurrency() {
        return _targetCurrency;
    }

    public void setTargetCurrency(Currency currency) {
        _targetCurrency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExchangeRate that = (ExchangeRate) o;
        return Objects.equals(_baseCurrency.getCode(), that._baseCurrency.getCode()) && Objects.equals(_targetCurrency.getCode(), that._targetCurrency.getCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(_baseCurrency.getCode(), _targetCurrency.getCode(), _rate);
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "baseCurrency=" + _baseCurrency +
                ", targetCurrency=" + _targetCurrency +
                ", rate=" + _rate +
                '}';
    }
}
