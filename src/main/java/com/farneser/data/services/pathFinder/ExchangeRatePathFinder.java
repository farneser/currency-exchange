package com.farneser.data.services.pathFinder;

import com.farneser.data.exceptions.InternalServerException;
import com.farneser.data.exceptions.NotFoundException;
import com.farneser.data.exceptions.ValueMissingException;
import com.farneser.data.models.Currency;
import com.farneser.data.models.ExchangeRate;
import com.farneser.data.services.AppDbContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class ExchangeRatePathFinder {
    private final List<ExchangeRate> _exchangeRates;

    public ExchangeRatePathFinder() {
        _exchangeRates = new ArrayList<>();
    }

    public ExchangeRatePathFinder(List<ExchangeRate> exchangeRates) {
        this();
        exchangeRates.forEach(e -> addExchangeRate(e.getBaseCurrency(), e.getTargetCurrency(), e.getRate()));
    }

    public static void main(String[] args) {

        var context = AppDbContext.getInstance();

        try {
            var er = context.exchangeRate.get();

            var pathFinder = new ExchangeRatePathFinder(er);

            var params = new HashMap<String, String>();

            params.put("code", "BTC");

            var c1 = context.currency.get(params).get(0);

            params.clear();
            params.put("code", "BYN");

            var c2 = context.currency.get(params).get(0);

            try {
                var path = pathFinder.findPath(c1, c2);
                System.out.println("Путь от " + c2.getCode() + " до " + c1.getCode() + ": " + path.stream().map(Currency::getCode).toList());

            } catch (NotFoundException e) {
                System.out.println("Путь не найден.");
            }


        } catch (InternalServerException | ValueMissingException | NotFoundException e) {
            System.out.println("error");
        }
    }

    public void addExchangeRate(Currency fromCurrency, Currency toCurrency, BigDecimal rate) {
        _addExchangeRate(fromCurrency, toCurrency, rate);
        _addExchangeRate(toCurrency, fromCurrency, BigDecimal.ONE.divide(rate, 10, RoundingMode.HALF_UP));
    }

    private void _addExchangeRate(Currency fromCurrency, Currency toCurrency, BigDecimal rate) {
        ExchangeRate exchangeRate = new ExchangeRate(fromCurrency, toCurrency, rate);
        _exchangeRates.add(exchangeRate);
    }

    public List<Currency> findPath(Currency startCurrency, Currency endCurrency) throws NotFoundException {
        var queue = new LinkedList<Currency>();
        var parentMap = new HashMap<Currency, Currency>();

        queue.add(startCurrency);
        parentMap.put(startCurrency, null);

        while (!queue.isEmpty()) {
            var currentCurrency = queue.poll();

            if (currentCurrency.equals(endCurrency)) {
                return buildPath(parentMap, endCurrency);
            }

            for (ExchangeRate rate : _exchangeRates) {
                if (rate.getBaseCurrency().equals(currentCurrency) && !parentMap.containsKey(rate.getTargetCurrency())) {
                    queue.add(rate.getTargetCurrency());
                    parentMap.put(rate.getTargetCurrency(), currentCurrency);
                }
            }
        }

        throw new NotFoundException();
    }

    private List<Currency> buildPath(Map<Currency, Currency> parentMap, Currency endCurrency) {
        var path = new ArrayList<Currency>();
        var currentCurrency = endCurrency;

        while (currentCurrency != null) {
            path.add(currentCurrency);
            currentCurrency = parentMap.get(currentCurrency);
        }

        Collections.reverse(path);

        return path;
    }
}
