package com.weather.util;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author akif
 * @since 1/15/22
 */
public class ReflectionUtils {

    public static <T> List<Field> getJsonIgnoredFieldList(T tClass) {
        Field[] fields = tClass.getClass().getDeclaredFields();
        List<Field> jsonIgnoredFields = new ArrayList<>();

        for (Field field : fields) {
            field.setAccessible(true);

            JsonIgnore jsonIgnore = field.getAnnotation(JsonIgnore.class);

            if (jsonIgnore != null && jsonIgnore.value()) {
                jsonIgnoredFields.add(field);
            }
        }

        return jsonIgnoredFields;
    }

    public static <T> Object getFieldValue(T tClass, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = tClass.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);

        return field.get(tClass);
    }
}
