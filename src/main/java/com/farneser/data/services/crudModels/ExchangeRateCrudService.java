package com.farneser.data.services.crudModels;

import com.farneser.data.models.ExchangeRate;
import com.farneser.data.services.CrudService;

import java.sql.Connection;
import java.util.List;

public class ExchangeRateCrudService extends CrudService<ExchangeRate> {

    public ExchangeRateCrudService(Connection connection) {
        super(connection, "ExchangeRates");
    }

    @Override
    public void create(ExchangeRate obj) {

    }

    @Override
    public void update(ExchangeRate obj) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public List<ExchangeRate> get() {
        return null;
    }

    @Override
    public ExchangeRate get(int id) {
        return null;
    }
}
