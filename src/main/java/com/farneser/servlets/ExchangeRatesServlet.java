package com.farneser.servlets;

import com.farneser.data.exceptions.InternalServerException;
import com.farneser.data.exceptions.NotFoundException;
import com.farneser.data.exceptions.UniqueConstraintException;
import com.farneser.data.exceptions.ValueMissingException;
import com.farneser.data.models.ErrorMessage;
import com.farneser.data.models.ExchangeRate;
import com.farneser.data.services.AppDbContext;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        var context = AppDbContext.getInstance();

        try {
            var writer = resp.getWriter();

            resp.setStatus(HttpServletResponse.SC_OK);
            writer.print(new Gson().toJson(context.exchangeRate.get()));
            writer.flush();

        } catch (InternalServerException e) {
            returnError(resp, ErrorMessage.InternalServerError);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        var context = AppDbContext.getInstance();

        var map = req.getParameterMap();

        try {
            var baseCurrencyCodes = map.get("baseCurrencyCode");
            var targetCurrencyCodes = map.get("targetCurrencyCode");
            var rates = map.get("rate");

            if (baseCurrencyCodes == null || targetCurrencyCodes == null || rates == null) {
                returnError(resp, ErrorMessage.FormFieldMissingError);
            } else {
                var exchangeRate = context.exchangeRate.create(new ExchangeRate(baseCurrencyCodes[0], targetCurrencyCodes[0], Double.parseDouble(rates[0])));

                var writer = resp.getWriter();

                writer.write(exchangeRate.getSerialized());
                writer.flush();
            }
        } catch (InternalServerException e) {
            returnError(resp, ErrorMessage.InternalServerError);
        } catch (UniqueConstraintException e) {
            returnError(resp, ErrorMessage.ExchangeRateAlreadyExistsError);
        } catch (ValueMissingException e) {
            returnError(resp, ErrorMessage.FormFieldMissingError);
        } catch (NotFoundException e) {
            returnError(resp, ErrorMessage.CurrencyCodeNotFound);
        }

    }
}
