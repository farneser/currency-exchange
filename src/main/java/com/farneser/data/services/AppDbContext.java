package com.farneser.data.services;

import com.farneser.data.models.Currency;
import com.farneser.data.models.ExchangeRate;
import com.farneser.data.services.crud.ICrud;
import com.farneser.data.services.crud.crudModels.CurrencyCrudService;
import com.farneser.data.services.crud.crudModels.ExchangeRateCrudService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;

public class AppDbContext {

    private static AppDbContext _instance;
    public final ICrud<Currency> currency;
    public final ICrud<ExchangeRate> exchangeRate;

    private AppDbContext() {
        var connection = openConnection();
        currency = new CurrencyCrudService(connection);
        exchangeRate = new ExchangeRateCrudService(connection);
    }

    public static AppDbContext getInstance() {

        if (_instance == null) {
            _instance = new AppDbContext();
        }

        return _instance;
    }

    private Connection openConnection() {
        Connection conn;

        try {
            Class.forName("org.sqlite.JDBC");

            conn = DriverManager.getConnection("jdbc:sqlite:currency.db");

            var inputStream = new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("tables.sql")));

            var reader = new BufferedReader(inputStream);

            var line = new StringBuilder();

            Arrays.stream(reader.lines().toArray()).forEach(l -> line.append(" ").append(l));

            var tables = line.toString().split(";");

            for (var table : tables) {

                var state = conn.createStatement();

                state.execute(table);

                state.close();
            }


            var state = conn.createStatement();

            var result = state.executeQuery("SELECT id FROM CURRENCIES");

            var linesCount = 0;

            while (result.next()) {
                linesCount++;
            }

            result.close();
            state.close();


            if (linesCount == 0){
                state = conn.createStatement();

                state.execute("DELETE FROM main.ExchangeRates;");

                state.close();

                initDataBase(conn);

            }


        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    private void initDataBase(Connection connection) throws SQLException {
        var currenciesSql = "insert into main.Currencies (ID, Code, FullName, Sign)\n" +
                "values  (1, 'USD', 'US Dollar', '$'),\n" +
                "        (2, 'EUR', 'Euro', '€'),\n" +
                "        (3, 'JPY', 'Yen', '¥'),\n" +
                "        (4, 'GBP', 'Pound Sterling', '£'),\n" +
                "        (5, 'AUD', 'Australian Dollar', 'A$'),\n" +
                "        (6, 'RUB', 'Russian Ruble', '₽');";


        var exchangeRatesSql = "insert into main.ExchangeRates (ID, BaseCurrencyId, TargetCurrencyId, Rate)\n" +
                "values  (1, 6, 1, 0.011),\n" +
                "        (2, 6, 2, 0.0097),\n" +
                "        (3, 6, 3, 1.53),\n" +
                "        (4, 6, 4, 0.0082),\n" +
                "        (5, 6, 5, 0.016),\n" +
                "        (6, 1, 2, 0.92),\n" +
                "        (7, 1, 3, 145.16),\n" +
                "        (8, 1, 4, 0.78),\n" +
                "        (9, 1, 6, 95.2);";


        var state = connection.createStatement();

        state.execute(currenciesSql);

        state.close();

        state = connection.createStatement();

        state.execute(exchangeRatesSql);

        state.close();

    }
}
