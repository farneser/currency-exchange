package com.farneser.data.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class ErrorMessage {
    public static ErrorMessage InternalServerError = new ErrorMessage("Database unavailable", 500);
    public static ErrorMessage CurrencyCodeNotFound = new ErrorMessage("Currency code not found", 400);
    public static ErrorMessage CurrencyNotFound = new ErrorMessage("Currency not found", 404);
    public static ErrorMessage CurrencyAlreadyExistsError = new ErrorMessage("Currency already exists", 409);
    public static ErrorMessage FormFieldMissingError = new ErrorMessage("Form field missing", 400);
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
