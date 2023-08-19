package com.farneser.data.services.crudModels;

import com.farneser.data.exceptions.InternalServerException;
import com.farneser.data.models.Currency;
import com.farneser.data.models.ExchangeRate;
import com.farneser.data.services.CrudService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateCrudService extends CrudService<ExchangeRate> {

    public ExchangeRateCrudService(Connection connection) {
        super(connection, "ExchangeRates");
    }

    @Override
    public ExchangeRate create(ExchangeRate obj) {
        return null;
    }

    @Override
    public void update(ExchangeRate obj) {
        throw new RuntimeException("method must be implemented");
    }

    @Override
    public void delete(int id) {
        throw new RuntimeException("method must be implemented");
    }

    @Override
    public List<ExchangeRate> get() throws InternalServerException {

        var sql = "SELECT * FROM ExchangeRates JOIN main.Currencies baseCurrency on baseCurrency.ID = ExchangeRates.BaseCurrencyId JOIN main.Currencies targetCurrency on targetCurrency.ID = ExchangeRates.BaseCurrencyId";

        var result = new ArrayList<ExchangeRate>();

        try {
            for (var entity : getByCommand(sql)) {
                result.add(deserialize(entity));
            }

        } catch (SQLException e) {
            throw new InternalServerException();
        }

        return result;

    }

    @Override
    public ExchangeRate deserialize(List<String> object) {

        var exchangeRate = new ExchangeRate(Integer.parseInt(object.get(0)), Integer.parseInt(object.get(1)), Integer.parseInt(object.get(2)), Double.parseDouble(object.get(3)));

        exchangeRate.setBaseCurrency(new Currency(Integer.parseInt(object.get(4)), object.get(5), object.get(6), object.get(7)));
        exchangeRate.setTargetCurrency(new Currency(Integer.parseInt(object.get(8)), object.get(9), object.get(10), object.get(11)));

        return exchangeRate;
    }
}
