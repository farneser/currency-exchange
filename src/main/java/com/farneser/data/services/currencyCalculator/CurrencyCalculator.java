package com.farneser.data.services.currencyCalculator;

import com.farneser.data.models.Currency;

import java.math.BigDecimal;
import java.util.List;

public class CurrencyCalculator {
    private final List<Currency> _path;

    public CurrencyCalculator(List<Currency> path) {
        _path = path;
    }

    public BigDecimal getConvertedAmount() {
        return new BigDecimal(_path.size() + 1);
    }

}
