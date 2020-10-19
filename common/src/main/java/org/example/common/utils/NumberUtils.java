package org.example.common.utils;

public class NumberUtils {

    public static boolean isNullOrZero(final Number number) {
        return number == null ||
                (
                        number instanceof Integer ? number.intValue() == 0 :
                                number instanceof Long ? number.longValue() == 0 :
                                        number instanceof Double ? number.doubleValue() == 0 :
                                                number instanceof Short ? number.shortValue() == 0 :
                                                        number.floatValue() == 0
                );
    }

    public static boolean isNotNullOrZero(final Number number) {
        return !isNullOrZero(number);
    }
}
