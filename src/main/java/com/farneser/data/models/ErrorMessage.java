package com.farneser.data.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class ErrorMessage {
    @SerializedName("message")
    private final String _message;
    private transient final int _code;

    public ErrorMessage(String message) {
        _message = message;
        _code = 200;
    }

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

    public static ErrorMessage InternalServerError = new ErrorMessage("Database unavailable", 500);
    public static ErrorMessage CurrencyCodeNotFound = new ErrorMessage("Currency code отсутствует", 400);
    public static ErrorMessage CurrencyNotFound = new ErrorMessage("Валюта не найдена", 404);
}
