package com.farneser.servlets;

import com.farneser.data.exceptions.InternalServerException;
import com.farneser.data.exceptions.UniqueConstraintException;
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
            var codes = map.get("baseCurrencyCode");
            var names = map.get("targetCurrencyCode");
            var signs = map.get("rate");

            if (codes == null || names == null || signs == null) {
                returnError(resp, ErrorMessage.FormFieldMissingError);
            } else {
                var exchangeRate = context.exchangeRate.create(new ExchangeRate(1, 1, 1));

                var writer = resp.getWriter();

                writer.write(exchangeRate.getSerialized());
                writer.flush();
            }

        } catch (InternalServerException e) {
            returnError(resp, ErrorMessage.InternalServerError);
        } catch (UniqueConstraintException e) {
            returnError(resp, ErrorMessage.CurrencyAlreadyExistsError);
        }

    }
}
