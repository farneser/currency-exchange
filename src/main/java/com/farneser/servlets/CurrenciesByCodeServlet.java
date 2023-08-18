package com.farneser.servlets;

import com.farneser.data.exceptions.ValueMissingError;
import com.farneser.data.exceptions.InternalError;
import com.farneser.data.models.ErrorMessage;
import com.farneser.data.services.AppDbContext;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/currencies/*")
public class CurrenciesByCodeServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        var currencies = AppDbContext.getInstance().currency;

        try {
            var writer = resp.getWriter();

            resp.setStatus(HttpServletResponse.SC_OK);

            var id = req.getPathInfo().substring(1);

            writer.print(new Gson().toJson(currencies.get("code", id)));
            writer.flush();

        } catch (InternalError e) {
            returnError(resp, ErrorMessage.InternalServerError);
        } catch (ValueMissingError e) {
            returnError(resp, ErrorMessage.CurrencyCodeNotFound);
        }

    }
}
