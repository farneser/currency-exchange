package com.farneser.data.services.crudModels;

import com.farneser.data.exceptions.InternalServerException;
import com.farneser.data.exceptions.UniqueConstraintException;
import com.farneser.data.models.Currency;
import com.farneser.data.services.CrudService;

import java.sql.Connection;
import java.util.List;

public class CurrencyCrudService extends CrudService<Currency> {

    public CurrencyCrudService(Connection connection) {
        super(connection, "Currencies");
    }

    @Override
    public Currency create(Currency obj) throws InternalServerException, UniqueConstraintException {

        var id = create("INSERT INTO " + _tableName + " (Code, FullName, Sign) VALUES ('" + obj.getCode() + "', '" + obj.getFullName() + "', '" + obj.getSign() + "') RETURNING ID;");

        obj.setId(id);

        return obj;
    }

    @Override
    public void update(Currency obj) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public Currency deserialize(List<String> object) {
        return new Currency(Integer.parseInt(object.get(0)), object.get(1), object.get(2), object.get(3));
    }
}
