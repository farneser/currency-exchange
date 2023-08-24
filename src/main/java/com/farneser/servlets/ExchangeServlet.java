package com.farneser.servlets;

import com.farneser.data.exceptions.InternalServerException;
import com.farneser.data.exceptions.NotFoundException;
import com.farneser.data.exceptions.ValueMissingException;
import com.farneser.data.exclusionStrategy.ExchangeRateVmExclusionStrategy;
import com.farneser.data.models.Currency;
import com.farneser.data.models.ErrorMessage;
import com.farneser.data.models.ExchangeViewModel;
import com.farneser.data.services.AppDbContext;
import com.farneser.data.services.crud.ICrud;
import com.farneser.data.services.currencyCalculator.CurrencyCalculator;
import com.farneser.data.services.pathFinder.ExchangeRatePathFinder;
import com.farneser.data.utils.ParamUtils;
import com.google.gson.GsonBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;

@WebServlet("/exchange")
public class ExchangeServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        var context = AppDbContext.getInstance();

        var from = req.getParameter("from");
        var to = req.getParameter("to");
        var amount = req.getParameter("amount");

        if (!ParamUtils.isExchangeParamsValid(from, to, amount)) {
            returnError(resp, ErrorMessage.ExchangeFormFieldMissingError);
            return;
        }

        try {
            var writer = resp.getWriter();

            var exchangeRates = context.exchangeRate.get();

            var pathFinder = new ExchangeRatePathFinder(exchangeRates);

            var fromCurrency = getCurrency(from, context.currency);
            var toCurrency = getCurrency(to, context.currency);

            var path = pathFinder.findPath(fromCurrency, toCurrency);

            var calculator = new CurrencyCalculator(path);

            var gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .setExclusionStrategies(new ExchangeRateVmExclusionStrategy())
                    .create();

            writer.write(gson.toJson(new ExchangeViewModel(
                    fromCurrency,
                    toCurrency,
                    BigDecimal.valueOf(Double.parseDouble(amount)),
                    calculator.getConvertedAmount(BigDecimal.valueOf(Double.parseDouble(amount))))));

            writer.flush();

        } catch (InternalServerException e) {
            returnError(resp, ErrorMessage.InternalServerError);
        } catch (NotFoundException e) {
            returnError(resp, ErrorMessage.ExchangeCodesNotFound);
        } catch (ValueMissingException e) {
            returnError(resp, ErrorMessage.ExchangeFormFieldMissingError);
        }
    }

    private Currency getCurrency(String code, ICrud<Currency> service) throws InternalServerException, ValueMissingException, NotFoundException {

        var params = new HashMap<String, String>();

        params.put("code", code.toUpperCase());

        return service.get(params).get(0);
    }
}
