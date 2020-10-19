package org.example.common.utils;

import java.lang.reflect.Array;

public final class ArrayUtils {

    public static <T> boolean isEmpty(final T[] array) {
        return getLength(array) == 0;
    }

    public static <T> boolean isNotEmpty(final T[] array) {
        return !isEmpty(array);
    }

    public static int getLength(final Object array) {
        if (array == null) {
            return 0;
        }
        return Array.getLength(array);
    }
}
