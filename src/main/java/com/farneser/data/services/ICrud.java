package com.farneser.data.services;

import com.farneser.data.exceptions.ValueMissingError;
import com.farneser.data.exceptions.InternalError;
import com.farneser.data.models.BaseEntity;

import java.util.List;

public interface ICrud<T extends BaseEntity> {
    void create(T obj) throws InternalError;

    void update(T obj);

    void delete(int id);

    List<T> get() throws InternalError;

    /**
     * get T object by only id (integer)
     *
     * @param id define index of searchable object
     * @return type object or null
     * @throws InternalError     on any server errors (database unavailable for example)
     * @throws ValueMissingError on missing or empty value
     */
    T get(int id) throws InternalError, ValueMissingError;

    /**
     * get T object by param and value (first or null)
     *
     * @param paramName define by which parameter we will search for the value
     * @param value     define value of parameter which we use for search
     * @return type object or null
     * @throws InternalError     on any server errors (database unavailable for example)
     * @throws ValueMissingError on missing or empty value
     */
    T get(String paramName, String value) throws InternalError, ValueMissingError;

    T deserialize(List<String> object) throws InternalError;
}
