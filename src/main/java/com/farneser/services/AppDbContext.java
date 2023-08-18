package com.farneser.services;

import com.farneser.models.Currency;
import com.farneser.models.ExchangeRate;
import com.farneser.services.crudModels.CurrencyCrudService;
import com.farneser.services.crudModels.ExchangeRateCrudService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;

public class AppDbContext {

    public final ICrud<Currency> currency;
    public final ICrud<ExchangeRate> exchangeRate;

    private Connection openConnection() {
        Connection conn;

        try {
            Class.forName("org.sqlite.JDBC");

            conn = DriverManager.getConnection("jdbc:sqlite:currency.db");

            var state = conn.createStatement();

            var inputStream = new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("tables.sql")));

            var reader = new BufferedReader(inputStream);

            var line = new StringBuilder();

            Arrays.stream(reader.lines().toArray()).forEach(l -> line.append(" ").append(l));

            state.execute(line.toString());
            state.close();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    private AppDbContext() {
        var connection = openConnection();
        currency = new CurrencyCrudService(connection);
        exchangeRate = new ExchangeRateCrudService(connection);
    }

    private static AppDbContext _instance;

    public static AppDbContext getInstance() {

        if (_instance == null) {
            _instance = new AppDbContext();
        }

        return _instance;
    }
}
