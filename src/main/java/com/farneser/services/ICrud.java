package com.farneser.services;

import java.util.List;

public interface ICrud<T> {
    void create(T obj);
    void update(T obj);
    void delete(int id);
    List<T> get();
    T get(int id);
}
