package com.farneser.models;

public class Currency {
    private int _id;
    private String _code;
    private String _fullName;
    private String _sing;

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

    public String getSing() {
        return _sing;
    }

    public void setSing(String sing) {
        _sing = sing;
    }
}
