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
    public ExchangeRate create(ExchangeRate obj) {
        return null;
    }

    @Override
    public void update(ExchangeRate obj) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public ExchangeRate deserialize(List<String> object){
        return null;
    }
}
