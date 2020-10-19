package org.example.common.utils;

import java.util.Collection;

public final class CollectionUtils {

    public static boolean isNotEmpty(Collection collection)
    {
        if(null == collection)
        {
            return false;
        }

        return !collection.isEmpty();
    }

    public static boolean isEmpty(Collection collection)
    {
        return !isNotEmpty(collection);
    }

}
