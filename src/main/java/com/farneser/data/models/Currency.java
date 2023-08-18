package com.farneser.data.models;

import com.google.gson.annotations.SerializedName;

public class Currency extends BaseEntity{
    @SerializedName("name")
    private String _fullName;
    @SerializedName("code")
    private String _code;
    @SerializedName("sign")
    private String _sign;

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
