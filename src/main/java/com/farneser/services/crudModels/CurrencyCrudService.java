package com.farneser.services.crudModels;

import com.farneser.models.Currency;
import com.farneser.services.CrudService;

import java.sql.Connection;

public class CurrencyCrudService extends CrudService<Currency> {

    public CurrencyCrudService(Connection connection) {
        super(connection, "Currencies");
    }
}
