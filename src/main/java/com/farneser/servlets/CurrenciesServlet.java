package com.farneser.servlets;

import com.farneser.data.exceptions.InternalServerException;
import com.farneser.data.exceptions.UniqueConstraintException;
import com.farneser.data.models.Currency;
import com.farneser.data.models.ErrorMessage;
import com.farneser.data.services.AppDbContext;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;

@WebServlet("/currencies")
public class CurrenciesServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        var currencies = AppDbContext.getInstance().currency;

        try {
            var writer = resp.getWriter();

            resp.setStatus(HttpServletResponse.SC_OK);
            writer.print(new Gson().toJson(currencies.get()));
            writer.flush();

        } catch (InternalServerException e) {
            returnError(resp, ErrorMessage.InternalServerError);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var currencies = AppDbContext.getInstance().currency;

        var map = req.getParameterMap();

        try {
            var codes = map.get("code");
            var names = map.get("name");
            var signs = map.get("sign");

            if (codes == null || names == null || signs == null) {
                returnError(resp, ErrorMessage.FormFieldMissingError);
            } else {
                var currency = currencies.create(new Currency(codes[0], names[0], signs[0]));

                var writer = resp.getWriter();

                writer.write(currency.getSerialized());
                writer.flush();
            }

        } catch (InternalServerException e) {
            returnError(resp, ErrorMessage.InternalServerError);
        } catch (UniqueConstraintException e) {
            returnError(resp, ErrorMessage.CurrencyAlreadyExistsError);
        }

    }
}
