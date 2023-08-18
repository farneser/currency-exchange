package com.farneser.servlets;

import com.farneser.data.models.ErrorMessage;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class BaseServlet extends HttpServlet {

    protected void returnError(HttpServletResponse resp, ErrorMessage errorMessage) throws IOException {
        var writer = resp.getWriter();
        resp.setStatus(errorMessage.getCode());
        writer.write(errorMessage.toString());
        writer.flush();
    }
}
