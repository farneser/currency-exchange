package com.farneser.servlets;

import com.farneser.data.exceptions.InvalidArgumentException;
import com.farneser.data.models.ErrorMessage;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseServlet extends HttpServlet {

    protected void returnError(HttpServletResponse resp, ErrorMessage errorMessage) throws IOException {
        var writer = resp.getWriter();
        resp.setStatus(errorMessage.getCode());
        writer.write(errorMessage.toString());
        writer.flush();
    }

    protected HashMap<String, List<String>> getBody(HttpServletRequest req) throws IOException, InvalidArgumentException {
        var body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        var pairs = body.split("\\&");
        var result = new HashMap<String, List<String>>();

        for (var pair : pairs) {
            var fields = pair.split("=");
            try {


                var name = URLDecoder.decode(fields[0], StandardCharsets.UTF_8);
                var value = URLDecoder.decode(fields[1], StandardCharsets.UTF_8);

                if (result.containsKey(name)) {
                    var line = result.get(name);

                    line.add(value);

                    result.put(name, line);
                } else {
                    var line = new ArrayList<String>();
                    line.add(value);
                    result.put(name, line);

                }


            } catch (ArrayIndexOutOfBoundsException e) {
                throw new InvalidArgumentException();
            }

        }

        return result;

    }
}
