package com.farneser.data.services;

import com.farneser.data.exceptions.InternalServerException;
import com.farneser.data.exceptions.NotFoundException;
import com.farneser.data.exceptions.UniqueConstraintException;
import com.farneser.data.exceptions.ValueMissingException;
import com.farneser.data.models.BaseEntity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public abstract class CrudService<T extends BaseEntity> implements ICrud<T> {
    protected final Connection _connection;
    protected String _tableName;

    public CrudService(Connection connection, String tableName) {
        _connection = connection;
        _tableName = tableName;
    }

    protected void executeQuery(String execute) throws InternalServerException, UniqueConstraintException {
        try {
            var preparedStatement = _connection.prepareStatement(execute);

            preparedStatement.execute();

            preparedStatement.close();

        } catch (SQLException e) {
            // 19 - [SQLITE_CONSTRAINT_UNIQUE] A UNIQUE constraint failed
            if (e.getErrorCode() == 19) {
                throw new UniqueConstraintException();
            }

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

        var sql = "SELECT * FROM $tableName;";

        sql = sql.replace("$tableName", _tableName);

        return getByCommand(sql);
    }

    protected T getFirst(HashMap<String, String> params) throws InternalServerException, ValueMissingException, NotFoundException {

        var result = get(params);

        if (result.isEmpty()) {
            throw new NotFoundException();
        }

        return result.get(0);
    }

    protected List<List<String>> getByCommand(String execute) throws SQLException {
        var preparedStatement = _connection.prepareStatement(execute);

        var queryResult = preparedStatement.executeQuery();

        var result = getValuesFromQuery(queryResult);

        preparedStatement.close();

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

        resultSet.close();

        return result;
    }

    @Override
    public T get(int id) throws InternalServerException, ValueMissingException, NotFoundException {
        var params = new HashMap<String, String>();

        params.put("id", String.valueOf(id));

        return getFirst(params);
    }

    @Override
    public List<T> get(HashMap<String, String> params) throws InternalServerException, ValueMissingException, NotFoundException {

        var isEmpty = new AtomicBoolean(false);

        params.forEach((param, value) -> {
            if (value.isEmpty()) {
                isEmpty.set(true);
            }
        });

        if (isEmpty.get() || params.isEmpty()) {
            throw new ValueMissingException();
        }

        try {
            var sql = new AtomicReference<>("SELECT * FROM %s");

            sql.set(String.format(sql.get(), _tableName));

            sql.set(sql.get() + " WHERE ");

            params.forEach((param, value) -> sql.set(sql.get() + param + "='" + value + "' and "));

            sql.set(sql.get().substring(0, sql.get().length() - 5) + ";");

            var preparedStatement = _connection.prepareStatement(sql.get(), Statement.RETURN_GENERATED_KEYS);

            var queryResult = preparedStatement.executeQuery();

            var entities = new ArrayList<T>();

            for (var entity : getValuesFromQuery(queryResult)) {
                entities.add(deserialize(entity));
            }

            queryResult.close();
            preparedStatement.close();

            if (!entities.isEmpty()) {
                return entities;
            }

            throw new NotFoundException();
        } catch (SQLException e) {
            throw new InternalServerException();
        }
    }
}
