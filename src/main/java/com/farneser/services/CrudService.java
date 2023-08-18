package com.farneser.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public abstract class CrudService<T> implements ICrud<T> {
    protected final Connection _connection;
    protected String _tableName = null;

    public CrudService(Connection connection) {
        _connection = connection;
    }

    public CrudService(Connection connection, String tableName) {
        this(connection);
        _tableName = tableName;
    }

    protected void create(String execute) {
        try {
            var state = _connection.createStatement();

            state.execute(execute);

            state.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
