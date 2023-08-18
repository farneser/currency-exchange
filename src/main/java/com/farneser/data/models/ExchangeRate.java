package com.farneser.data.models;

import java.math.BigInteger;

public class ExchangeRate extends BaseEntity {
    private int _baseCurrencyId;
    private int _targetCurrencyId;
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
