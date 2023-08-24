package com.farneser.data.services.currencyCalculator;

import com.farneser.data.exceptions.InternalServerException;
import com.farneser.data.exceptions.NotFoundException;
import com.farneser.data.exceptions.ValueMissingException;
import com.farneser.data.models.Currency;
import com.farneser.data.services.AppDbContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;

public class CurrencyCalculator {
    private final List<Currency> _path;

    public CurrencyCalculator(List<Currency> path) {
        _path = path;
    }

    public BigDecimal getConvertedAmount(BigDecimal amount) throws InternalServerException, ValueMissingException, NotFoundException {
        var context = AppDbContext.getInstance();

        var queue = new ArrayDeque<>(_path);

        var currentCurrency = queue.pop();

        var convertedAmount = amount;

        while (!queue.isEmpty()) {
            var params = new HashMap<String, String>();

            params.put("baseCurrency", currentCurrency.getCode());

            currentCurrency = queue.pop();

            params.put("targetCurrency", currentCurrency.getCode());

            var exchangeRate = context.exchangeRate.get(params);

            if (!exchangeRate.get(0).getBaseCurrency().getCode().equals(currentCurrency.getCode())) {
                convertedAmount = convertedAmount.multiply(exchangeRate.get(0).getRate());
            } else {
                var reversed = BigDecimal.ONE.divide(exchangeRate.get(0).getRate(), 6, RoundingMode.HALF_UP);
                convertedAmount = convertedAmount.multiply(reversed);
            }
        }

        return convertedAmount;
    }

}
