package com.farneser.servlets;

import com.farneser.data.exceptions.InternalServerException;
import com.farneser.data.exceptions.NotFoundException;
import com.farneser.data.exceptions.ValueMissingException;
import com.farneser.data.models.ErrorMessage;
import com.farneser.data.services.AppDbContext;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;

@WebServlet("/currency/*")
public class CurrencyServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        var context = AppDbContext.getInstance();

        try {
            var writer = resp.getWriter();

            resp.setStatus(HttpServletResponse.SC_OK);

            // endpoint must have only 3 letters of country code
            // examples or req.getPathInfo: null, /BTC, /, /HELLO
            // only /BTC correct
            if (req.getPathInfo() == null || req.getPathInfo().length() != 4) {
                throw new ValueMissingException();
            }

            var code = req.getPathInfo().substring(1);

            var params = new HashMap<String, String>();

            params.put("code", code.toUpperCase());

            writer.print(new Gson().toJson(context.currency.get(params).get(0)));
            writer.flush();

        } catch (InternalServerException e) {
            returnError(resp, ErrorMessage.InternalServerError);
        } catch (ValueMissingException e) {
            returnError(resp, ErrorMessage.CurrencyCodeNotFound);
        } catch (NotFoundException e) {
            returnError(resp, ErrorMessage.CurrencyNotFound);
        }

    }
}
