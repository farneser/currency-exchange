package com.farneser.models;

import com.google.gson.annotations.SerializedName;

public class Currency {
    @SerializedName("id")
    private int _id;
    @SerializedName("name")
    private String _fullName;
    @SerializedName("code")
    private String _code;
    @SerializedName("sign")
    private String _sign;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    public String getCode() {
        return _code;
    }

    public void setCode(String code) {
        _code = code;
    }

    public String getFullName() {
        return _fullName;
    }

    public void setFullName(String fullName) {
        _fullName = fullName;
    }

    public String getSign() {
        return _sign;
    }

    public void setSign(String sing) {
        _sign = sing;
    }

    public Currency(String code, String fullName, String sign) {
        _code = code;
        _fullName = fullName;
        _sign = sign;
    }

    public Currency(int id, String code, String fullName, String sign) {
        this(code, fullName, sign);
        _id = id;
    }
}
