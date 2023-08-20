package com.farneser.data.utils;

public abstract class ParamUtils {

    public static boolean isExchangeParamsValid(String from, String to, String amount) {

        if (from == null || to == null || amount == null)
            return false;

        if (from.isEmpty() || to.isEmpty() || amount.isEmpty())
            return false;

        if (from.length() != 3 || to.length() != 3)
            return false;

        try {
            Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }
}
