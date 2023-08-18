package com.farneser.services;

import java.sql.Connection;
import java.util.List;

public class CrudService<T> implements ICrud<T> {
    protected final Connection _connection;
    protected String _tableName = null;
    public CrudService(Connection connection) {
        _connection = connection;
    }

    public CrudService(Connection connection, String tableName){
        this(connection);
        _tableName = tableName;
    }

    @Override
    public void create(T obj) {

    }

    @Override
    public void update(T obj) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public List<T> get() {
        return null;
    }

    @Override
    public T get(int id) {
        return null;
    }
}
