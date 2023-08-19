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
import java.util.List;

public abstract class CrudService<T extends BaseEntity> implements ICrud<T> {
    protected final Connection _connection;
    protected String _tableName;

    public CrudService(Connection connection, String tableName) {
        _connection = connection;
        _tableName = tableName;
    }

    protected int create(String execute) throws InternalServerException, UniqueConstraintException {
        try {
            var state = _connection.createStatement();

            var id = state.executeQuery(execute);

            for (var val : getValuesFromQuery(id)) {
                return Integer.parseInt(val.get(0));
            }

            state.close();

            return 0;

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
            var sql = "SELECT * FROM %s WHERE %s=?";

            sql = String.format(sql, _tableName, paramName);

            var preparedStatement = _connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, "'" + value + "'");

            var queryResult = preparedStatement.executeQuery();

            for (var entity : getValuesFromQuery(queryResult)) {
                return deserialize(entity);
            }

            queryResult.close();
            preparedStatement.close();

            throw new NotFoundException();
        } catch (SQLException e) {
            throw new InternalServerException();
        }
    }
}
