package com.farneser.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Currency extends BaseEntity {
    @SerializedName("name")
    private String _fullName;
    @SerializedName("code")
    private String _code;
    @SerializedName("sign")
    private String _sign;

    public Currency(String code, String fullName, String sign) {
        _code = code;
        _fullName = fullName;
        _sign = sign;
    }

    public Currency(int id, String code, String fullName, String sign) {
        this(code, fullName, sign);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return Objects.equals(_fullName, currency._fullName) && Objects.equals(_code, currency._code) && Objects.equals(_sign, currency._sign);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_fullName, _code, _sign);
    }
}
