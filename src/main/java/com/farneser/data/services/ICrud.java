package com.farneser.data.services;

import com.farneser.data.exceptions.InternalServerException;
import com.farneser.data.exceptions.NotFoundException;
import com.farneser.data.exceptions.UniqueConstraintException;
import com.farneser.data.exceptions.ValueMissingException;
import com.farneser.data.models.BaseEntity;
import com.farneser.data.models.ExchangeRate;

import java.util.HashMap;
import java.util.List;

public interface ICrud<T extends BaseEntity> {
    /**
     * method to create new entity
     *
     * @param obj instance of object that we want to save
     * @return object T
     * @throws InternalServerException   in any server error
     * @throws UniqueConstraintException in unique error (id already exists for example)
     */
    T create(T obj) throws InternalServerException, UniqueConstraintException, ValueMissingException, NotFoundException;

    ExchangeRate update(T obj) throws InternalServerException, ValueMissingException, NotFoundException, UniqueConstraintException;

    void delete(int id);

    /**
     * method to get all entities of type T
     *
     * @return the list of all objects T
     * @throws InternalServerException on any server error
     */
    List<T> get() throws InternalServerException;

    /**
     * get T object by only id (integer)
     *
     * @param id define index of searchable object
     * @return type object or null
     * @throws InternalServerException on any server errors (database unavailable for example)
     * @throws ValueMissingException   on missing or empty value
     */
    T get(int id) throws InternalServerException, ValueMissingException, NotFoundException;

    /**
     * get List<T> objects by params and values (search, select first or default)
     *
     * @param params define hashmap with param-value map
     * @return type object or null
     * @throws InternalServerException on any server errors (database unavailable for example)
     * @throws ValueMissingException   on missing or empty value
     */
    List<T> get(HashMap<String, String> params) throws InternalServerException, ValueMissingException, NotFoundException;

    T deserialize(List<String> object) throws InternalServerException;
}
