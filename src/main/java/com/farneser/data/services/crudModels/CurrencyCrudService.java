package com.farneser.data.services.crudModels;

import com.farneser.data.exceptions.InternalError;
import com.farneser.data.models.Currency;
import com.farneser.data.services.CrudService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrencyCrudService extends CrudService<Currency> {

    public CurrencyCrudService(Connection connection) {
        super(connection, "Currencies");
    }

    @Override
    public void create(Currency obj) throws InternalError {
        create("INSERT INTO " + _tableName + " (Code, FullName, Sign) VALUES ('" + obj.getCode() + "', '" + obj.getFullName() + "', '" + obj.getSign() + "');");
    }

    @Override
    public void update(Currency obj) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public List<Currency> get() throws InternalError {
        var result = new ArrayList<Currency>();

        try {
            var entities = getAll();

            for (var entity : entities) {

                result.add(new Currency(Integer.parseInt(entity.get(0)), entity.get(1), entity.get(2), entity.get(3)));
            }

        } catch (SQLException e) {
            throw new InternalError();
        }

        return result;

    }

    @Override
    public Currency get(int id) {
        return null;
    }
}
