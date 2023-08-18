package com.farneser.data.services;

import com.farneser.data.exceptions.InternalServerException;
import com.farneser.data.exceptions.NotFoundException;
import com.farneser.data.exceptions.ValueMissingException;
import com.farneser.data.models.BaseEntity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class CrudService<T extends BaseEntity> implements ICrud<T> {
    protected final Connection _connection;
    protected String _tableName;

    public CrudService(Connection connection, String tableName) {
        _connection = connection;
        _tableName = tableName;
    }

    protected void create(String execute) throws InternalServerException {
        try {
            var state = _connection.createStatement();

            state.execute(execute);

            state.close();

        } catch (SQLException e) {
            throw new InternalServerException();
        }
    }

    @Override
    public List<T> get() throws InternalServerException {
        var result = new ArrayList<T>();

        try {
            var entities = getAll();

            for (var entity : entities) {

                result.add(deserialize(entity));
            }

        } catch (SQLException e) {
            throw new InternalServerException();
        }

        return result;
    }

    protected List<List<String>> getAll() throws SQLException {

        var state = _connection.createStatement();

        var queryResult = state.executeQuery("SELECT * FROM " + _tableName);

        var result = getValuesFromQuery(queryResult);

        state.close();

        return result;
    }

    protected List<List<String>> getValuesFromQuery(ResultSet resultSet) throws SQLException {
        var result = new ArrayList<List<String>>();

        var columns = resultSet.getMetaData().getColumnCount();

        while (resultSet.next()) {
            var line = new ArrayList<String>();

            for (int i = 1; i <= columns; i++) {
                line.add(resultSet.getString(i));
            }

            result.add(line);
        }

        return result;
    }

    @Override
    public T get(int id) throws InternalServerException, ValueMissingException, NotFoundException {
        return get("id", String.valueOf(id));
    }

    @Override
    public T get(String paramName, String value) throws InternalServerException, ValueMissingException, NotFoundException {

        if (value.isEmpty()) {
            throw new ValueMissingException();
        }

        try {
            var state = _connection.createStatement();

            var queryResult = state.executeQuery("SELECT * FROM " + _tableName + " WHERE " + paramName + "='" + value + "';");

            for (var entity : getValuesFromQuery(queryResult)) {
                return deserialize(entity);
            }

            state.close();

            throw new NotFoundException();
        } catch (SQLException e) {
            throw new InternalServerException();
        }
    }
}
