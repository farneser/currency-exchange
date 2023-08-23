package com.farneser.data.exclusionStrategy;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class ExchangeRateVmExclusionStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        return fieldAttributes.getName().equals("_id");
    }

    @Override
    public boolean shouldSkipClass(Class<?> aClass) {
        return false;
    }
}

