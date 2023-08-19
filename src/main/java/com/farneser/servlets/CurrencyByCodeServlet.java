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

@WebServlet("/currency/*")
public class CurrencyByCodeServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        var context = AppDbContext.getInstance();

        try {
            var writer = resp.getWriter();

            resp.setStatus(HttpServletResponse.SC_OK);

            if (req.getPathInfo() == null){
                throw new ValueMissingException();
            }

            var id = req.getPathInfo().substring(1);

            writer.print(new Gson().toJson(context.currency.get("code", id)));
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
