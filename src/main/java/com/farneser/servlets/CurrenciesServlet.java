package com.farneser.servlets;

import com.farneser.data.exceptions.InternalError;
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

        } catch (InternalError e) {
            returnError(resp, ErrorMessage.InternalServerError);
        }

    }
}
