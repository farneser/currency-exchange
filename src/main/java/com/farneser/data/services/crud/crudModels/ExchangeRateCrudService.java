package com.farneser.data.services.crud.crudModels;

import com.farneser.data.exceptions.InternalServerException;
import com.farneser.data.exceptions.NotFoundException;
import com.farneser.data.exceptions.UniqueConstraintException;
import com.farneser.data.exceptions.ValueMissingException;
import com.farneser.data.models.Currency;
import com.farneser.data.models.ExchangeRate;
import com.farneser.data.services.crud.AppDbContext;
import com.farneser.data.services.crud.CrudService;
import com.farneser.data.services.crud.ICrud;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ExchangeRateCrudService extends CrudService<ExchangeRate> {

    public ExchangeRateCrudService(Connection connection) {
        super(connection, "ExchangeRates");
    }

    @Override
    public ExchangeRate create(ExchangeRate obj) throws UniqueConstraintException, InternalServerException, ValueMissingException, NotFoundException {

        try {

            var params = new HashMap<String, String>();

            params.put("baseCurrency", obj.getTargetCurrency().getCode());
            params.put("targetCurrency", obj.getBaseCurrency().getCode());

            // if getFirst does not raise an error, then it is not found, and it will be caught,
            // if found, uniqueness exceptions will be raised
            getFirst(params);

            throw new UniqueConstraintException();

        } catch (NotFoundException e) {

            try {

                var sql =
                        "INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) \n" +
                        "SELECT id1, id2, ? \n" +
                        "FROM (SELECT C1.ID as id1, C2.ID as id2 \n" +
                        "      FROM (SELECT ID FROM Currencies WHERE Code = ?) AS C1 \n" +
                        "               CROSS JOIN(SELECT ID FROM Currencies WHERE (Code = ?)) AS C2); \n";

                var statement = _connection.prepareStatement(sql);

                statement.setString(1, obj.getRate().toString());
                statement.setString(2, obj.getBaseCurrency().getCode());
                statement.setString(3, obj.getTargetCurrency().getCode());

                executeQuery(statement);

            } catch (SQLException ex) {
                throw new InternalServerException();
            }

        }

        var params = new HashMap<String, String>();

        params.put("baseCurrency", obj.getBaseCurrency().getCode());
        params.put("targetCurrency", obj.getTargetCurrency().getCode());

        return getFirst(params);
    }

    @Override
    public ExchangeRate update(ExchangeRate obj) throws InternalServerException, ValueMissingException, NotFoundException, UniqueConstraintException {

        executeQuery(
                "UPDATE ExchangeRates\n" +
                        "SET Rate=" + obj.getRate() + "\n" +
                        "WHERE BaseCurrencyId = (SELECT C1.ID as id1\n" +
                        "                        FROM (SELECT ID FROM Currencies WHERE Code = '" + obj.getBaseCurrency().getCode() + "') AS C1\n" +
                        "                                 CROSS JOIN(SELECT ID FROM Currencies WHERE (Code = '" + obj.getTargetCurrency().getCode() + "')) AS C2)\n" +
                        "  and TargetCurrencyId = (SELECT C2.ID as id2\n" +
                        "                        FROM (SELECT ID FROM Currencies WHERE Code = '" + obj.getBaseCurrency().getCode() + "') AS C1\n" +
                        "                                 CROSS JOIN(SELECT ID FROM Currencies WHERE (Code = '" + obj.getTargetCurrency().getCode() + "')) AS C2)\n");

        var params = new HashMap<String, String>();

        params.put("baseCurrency", obj.getBaseCurrency().getCode());
        params.put("targetCurrency", obj.getTargetCurrency().getCode());

        return getFirst(params);

    }

    @Override
    public void delete(int id) {
        throw new RuntimeException("method must be implemented");
    }

    @Override
    public List<ExchangeRate> get() throws InternalServerException {

        var sql = "SELECT * FROM ExchangeRates JOIN main.Currencies C on ExchangeRates.BaseCurrencyId = C.ID JOIN main.Currencies C2 on ExchangeRates.TargetCurrencyId = C2.ID;";

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
    public List<ExchangeRate> get(HashMap<String, String> params) throws InternalServerException, ValueMissingException, NotFoundException {

        var isEmpty = new AtomicBoolean(false);

        params.forEach((param, value) -> {
            if (value.isEmpty()) {
                isEmpty.set(true);
            }
        });

        if (isEmpty.get() || params.size() < 2) {
            throw new ValueMissingException();
        }

        var result = new ArrayList<ExchangeRate>();

        try {

            var sql =
                    "SELECT * FROM ExchangeRates " +
                            "JOIN main.Currencies base ON ExchangeRates.BaseCurrencyId = base.ID " +
                            "JOIN main.Currencies target ON ExchangeRates.TargetCurrencyId = target.ID " +
                            "WHERE base.Code = ? AND target.Code = ? OR base.Code = ? AND target.Code = ?;";

            var preparedState = _connection.prepareStatement(sql);

            preparedState.setString(1, params.get("baseCurrency"));
            preparedState.setString(2, params.get("targetCurrency"));
            preparedState.setString(3, params.get("targetCurrency"));
            preparedState.setString(4, params.get("baseCurrency"));

            var queryResult = preparedState.executeQuery();

            var queryValues = getValuesFromQuery(queryResult);

            queryValues.forEach(val -> result.add(deserialize(val)));

            queryResult.close();
            preparedState.close();

        } catch (SQLException e) {
            throw new InternalServerException();
        }

        return result;
    }

    @Override
    public ExchangeRate deserialize(List<String> object) {

        var exchangeRate = new ExchangeRate(Integer.parseInt(object.get(0)), Integer.parseInt(object.get(1)), Integer.parseInt(object.get(2)), BigDecimal.valueOf(Double.parseDouble(object.get(3))));

        exchangeRate.setBaseCurrency(new Currency(Integer.parseInt(object.get(4)), object.get(5), object.get(6), object.get(7)));
        exchangeRate.setTargetCurrency(new Currency(Integer.parseInt(object.get(8)), object.get(9), object.get(10), object.get(11)));

        return exchangeRate;
    }

    private static Currency getCurrency(String code, ICrud<Currency> service) throws InternalServerException, ValueMissingException, NotFoundException {

        var params = new HashMap<String, String>();

        params.put("code", code);

        return service.get(params).get(0);
    }

    public static void main(String[] args) {
        var context = AppDbContext.getInstance();

        try {

            var byn = getCurrency("BYN", context.currency);
            var btc = getCurrency("BTC", context.currency);
            var pln = getCurrency("PLN", context.currency);

            context.exchangeRate.create(new ExchangeRate(byn, btc, BigDecimal.ONE));
            context.exchangeRate.create(new ExchangeRate(btc, byn, BigDecimal.ONE));
            context.exchangeRate.create(new ExchangeRate(pln, byn, BigDecimal.ONE));

        } catch (InternalServerException | ValueMissingException | NotFoundException | UniqueConstraintException e) {
            System.out.println("problems");
        }

    }
}
