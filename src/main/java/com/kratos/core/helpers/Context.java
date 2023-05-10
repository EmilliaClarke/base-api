package com.kratos.core.helpers;

import com.kratos.core.exception.ExceptionError;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Context {
    private static final ThreadLocal<Map<String, Object>> data = ThreadLocal.withInitial(HashMap::new);

    public static Object getSharedParameter(String key) {
        if (!hasSharedParameter(key)) {
            return new ExceptionError(String.format("There is no shared parameter with such key %s", key));
        }
        return data.get().get(key);
    }

    public static void saveSharedParameter(String key, Object value) {
        data.get().put(key, value);
    }

    public static boolean hasSharedParameter(String key) {
        return data.get().containsKey(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) data.get().get(key);
    }
}
