package com.farneser.services;

import com.farneser.exceptions.InternalError;

import java.util.List;

public interface ICrud<T> {
    void create(T obj);
    void update(T obj);
    void delete(int id);
    List<T> get() throws InternalError;
    T get(int id);
}
