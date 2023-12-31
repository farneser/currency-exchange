package com.farneser.servlets;

import com.farneser.data.exceptions.*;
import com.farneser.data.models.ErrorMessage;
import com.farneser.data.models.ExchangeRate;
import com.farneser.data.services.AppDbContext;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends PatchServlet {

    private static HashMap<String, String> getParams(HttpServletRequest req) throws ValueMissingException {

        // endpoint must have only 7 letters of country codes
        // examples or req.getPathInfo: null, /BTC, /, /HELLO, /BTCBYN
        // only /BTCBYN correct
        if (req.getPathInfo() == null || req.getPathInfo().length() != 7) {
            throw new ValueMissingException();
        }

        var id = req.getPathInfo().substring(1);

        var params = new HashMap<String, String>();

        params.put("baseCurrency", id.substring(0, 3).toUpperCase());
        params.put("targetCurrency", id.substring(3, 6).toUpperCase());

        return params;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        var context = AppDbContext.getInstance();

        try {
            var writer = resp.getWriter();

            resp.setStatus(HttpServletResponse.SC_OK);

            var params = getParams(req);

            writer.print(new Gson().toJson(context.exchangeRate.get(params).get(0)));
            writer.flush();

        } catch (InternalServerException e) {
            returnError(resp, ErrorMessage.InternalServerError);
        } catch (ValueMissingException e) {
            returnError(resp, ErrorMessage.CurrencyCodeNotFound);
        } catch (NotFoundException e) {
            returnError(resp, ErrorMessage.CurrencyNotFound);
        }

    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var context = AppDbContext.getInstance();

        try {
            var map = getBody(req);

            var rates = map.get("rate");

            var params = getParams(req);

            if (rates == null) {
                returnError(resp, ErrorMessage.ExchangeRatePatchFormFieldMissingError);
            } else {
                var exchangeRate = context.exchangeRate
                        .update(new ExchangeRate(
                                params.get("baseCurrency"),
                                params.get("targetCurrency"),
                                BigDecimal.valueOf(Double.parseDouble(rates.get(0)))));

                var writer = resp.getWriter();

                writer.write(exchangeRate.getSerialized());
                writer.flush();
            }
        } catch (ValueMissingException | InvalidArgumentException e) {
            returnError(resp, ErrorMessage.ExchangeRatePatchFormFieldMissingError);
        } catch (InternalServerException | UniqueConstraintException e) {
            returnError(resp, ErrorMessage.InternalServerError);
        } catch (NotFoundException e) {
            returnError(resp, ErrorMessage.ExchangeCodesNotFound);
        }
    }
}
