package com.farneser.services.crudModels;

import com.farneser.exceptions.InternalError;
import com.farneser.models.Currency;
import com.farneser.services.CrudService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrencyCrudService extends CrudService<Currency> {

    public CurrencyCrudService(Connection connection) {
        super(connection, "Currencies");
    }

    @Override
    public void create(Currency obj) {
        create("INSERT INTO " + _tableName + " (Code, FullName, Sign) VALUES ('" + obj.getCode() + "', '" + obj.getFullName() + "', '" + obj.getSing() + "');");
    }

    @Override
    public void update(Currency obj) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public List<Currency> get() throws InternalError {

        try {
            var state = _connection.createStatement();

            var result = state.executeQuery("SELECT * FROM " + _tableName);

            var columns = result.getMetaData().getColumnCount();

            while (result.next()) {
                var line = new ArrayList<String>();

                for (int i = 1; i <= columns; i++) {
                    line.add(result.getString(i));
                }

                System.out.println(line);
            }
            state.close();

        } catch (SQLException e) {
            throw new InternalError();
        }


        return null;

    }

    @Override
    public Currency get(int id) {
        return null;
    }
}
