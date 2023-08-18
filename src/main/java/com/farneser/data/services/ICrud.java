package com.farneser.data.services;

import com.farneser.data.exceptions.NotFoundException;
import com.farneser.data.exceptions.UniqueConstraintException;
import com.farneser.data.exceptions.ValueMissingException;
import com.farneser.data.exceptions.InternalServerException;
import com.farneser.data.models.BaseEntity;

import java.util.List;

public interface ICrud<T extends BaseEntity> {
    T create(T obj) throws InternalServerException, UniqueConstraintException;

    void update(T obj);

    void delete(int id);

    List<T> get() throws InternalServerException;

    /**
     * get T object by only id (integer)
     *
     * @param id define index of searchable object
     * @return type object or null
     * @throws InternalServerException     on any server errors (database unavailable for example)
     * @throws ValueMissingException on missing or empty value
     */
    T get(int id) throws InternalServerException, ValueMissingException, NotFoundException;

    /**
     * get T object by param and value (first or null)
     *
     * @param paramName define by which parameter we will search for the value
     * @param value     define value of parameter which we use for search
     * @return type object or null
     * @throws InternalServerException     on any server errors (database unavailable for example)
     * @throws ValueMissingException on missing or empty value
     */
    T get(String paramName, String value) throws InternalServerException, ValueMissingException, NotFoundException;

    T deserialize(List<String> object) throws InternalServerException;
}
