package com.farneser.services.crudModels;

import com.farneser.models.ExchangeRate;
import com.farneser.services.CrudService;

import java.sql.Connection;

public class ExchangeRateCrudService extends CrudService<ExchangeRate> {

    public ExchangeRateCrudService(Connection connection) {
        super(connection, "ExchangeRates");
    }
}
