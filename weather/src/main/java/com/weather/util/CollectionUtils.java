package com.weather.util;

import java.util.*;

/**
 * @author akif
 * @since 1/14/22
 */
public class CollectionUtils {

    public static <T> boolean hasDuplicate(List<T> list) {
        Set<T> set = new HashSet<>(nullSafeList(list));

        return set.size() < list.size();
    }

    public static <T> List<T> nullSafeList(List<T> list) {
        return isNotEmpty(list) ? list : new ArrayList<>();
    }

    public static <T> boolean isNotEmpty(Collection<T> collection) {
        return !isEmpty(collection);
    }

    public static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.size() == 0;
    }
}
