package com.farneser.data.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class ErrorMessage {
    public static ErrorMessage InternalServerError = new ErrorMessage("Database unavailable", 500);
    public static ErrorMessage CurrencyCodeNotFound = new ErrorMessage("Currency code not found, try /currency/USD", 400);
    public static ErrorMessage CurrencyNotFound = new ErrorMessage("Currency not found", 404);
    public static ErrorMessage CurrencyAlreadyExistsError = new ErrorMessage("Currency already exists", 409);
    public static ErrorMessage CurrenciesFormFieldMissingError = new ErrorMessage("Form field missing, try: name=NAME&code=CODE&sign=SIGN", 400);
    public static ErrorMessage ExchangeRatesFormFieldMissingError = new ErrorMessage("Form field missing, try baseCurrencyCode=CODE&targetCurrencyCode=CODE&rate=RATE", 400);
    public static ErrorMessage ExchangeFormFieldMissingError = new ErrorMessage("Form field missing, try /exchange?from=USD&to=AUD&amount=10", 400);
    public static ErrorMessage ExchangeRatePatchFormFieldMissingError = new ErrorMessage("Form field missing, try rate=RATE", 400);
    public static ErrorMessage ExchangeCodesNotFound = new ErrorMessage("Exchange codes not found", 404);
    public static ErrorMessage ExchangeRateAlreadyExistsError = new ErrorMessage("Exchange rate already exists", 409);
    @SerializedName("message")
    private final String _message;
    private transient final int _code;

    public ErrorMessage(String message, int code) {
        _message = message;
        _code = code;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public int getCode() {
        return _code;
    }
}
