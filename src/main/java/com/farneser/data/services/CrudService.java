package com.farneser.data.services;

import com.farneser.data.exceptions.InternalError;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class CrudService<T> implements ICrud<T> {
    protected final Connection _connection;
    protected String _tableName;

    public CrudService(Connection connection, String tableName) {
        _connection = connection;
        _tableName = tableName;
    }

    protected void create(String execute) throws InternalError {
        try {
            var state = _connection.createStatement();

            state.execute(execute);

            state.close();

        } catch (SQLException e) {
            throw new InternalError();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    protected List<List<String>> getAll() throws SQLException {
        var result = new ArrayList<List<String>>();

        var state = _connection.createStatement();

        var queryResult = state.executeQuery("SELECT * FROM " + _tableName);

        var columns = queryResult.getMetaData().getColumnCount();

        while (queryResult.next()) {
            var line = new ArrayList<String>();

            for (int i = 1; i <= columns; i++) {
                line.add(queryResult.getString(i));
            }

            result.add(line);
        }

        state.close();

        return result;
    }

    protected List<String> getById() {
        return null;
    }

}
