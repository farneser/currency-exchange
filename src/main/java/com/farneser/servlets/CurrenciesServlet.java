package com.farneser.servlets;

import com.farneser.exceptions.InternalError;
import com.farneser.models.ErrorMessage;
import com.farneser.services.AppDbContext;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;

@WebServlet("/currencies")
public class CurrenciesServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Content-type", "text/json");

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
