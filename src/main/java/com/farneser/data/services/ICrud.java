package com.farneser.data.services;

import com.farneser.data.exceptions.InternalError;

import java.util.List;

public interface ICrud<T> {
    void create(T obj) throws InternalError;
    void update(T obj);
    void delete(int id);
    List<T> get() throws InternalError;
    T get(int id);
}
