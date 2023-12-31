package com.farneser.data.services.crud.crudModels;

import com.farneser.data.exceptions.InternalServerException;
import com.farneser.data.exceptions.NotFoundException;
import com.farneser.data.exceptions.UniqueConstraintException;
import com.farneser.data.exceptions.ValueMissingException;
import com.farneser.data.models.Currency;
import com.farneser.data.models.ExchangeRate;
import com.farneser.data.services.crud.CrudService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class CurrencyCrudService extends CrudService<Currency> {

    public CurrencyCrudService(Connection connection) {
        super(connection, "Currencies");
    }

    @Override
    public Currency create(Currency obj) throws InternalServerException, UniqueConstraintException, ValueMissingException, NotFoundException {

        try {
            var state = _connection.prepareStatement(
                    """
                            INSERT INTO Currencies (Code, FullName, Sign) VALUES (?, ?, ?) RETURNING ID;
                            """);

            state.setString(1, obj.getCode());
            state.setString(2, obj.getFullName());
            state.setString(3, obj.getSign());

            executeQuery(state);

            state.close();

        } catch (SQLException e) {
            throw new InternalServerException();
        }

        var params = new HashMap<String, String>();

        params.put("code", obj.getCode());

        return getFirst(params);
    }


    @Override
    public ExchangeRate update(Currency obj) {
        throw new RuntimeException("method must be implemented");
    }

    @Override
    public void delete(int id) {
        throw new RuntimeException("method must be implemented");
    }

    @Override
    public Currency deserialize(List<String> object) {
        return new Currency(Integer.parseInt(object.get(0)), object.get(1), object.get(2), object.get(3));
    }
}
