package com.farneser.data.models;

import com.google.gson.annotations.SerializedName;

public class BaseEntity {
    @SerializedName("id")
    protected int _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
}

