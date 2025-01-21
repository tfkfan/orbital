package com.tfkfan.webgame.route;

import com.tfkfan.webgame.session.UserSession;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class RouteProcessor {
    private final Map<Integer, Method> methods;
    private final Object source;

    public RouteProcessor(Object obj) {
        this.source = obj;
        methods = new HashMap<>();
        Class<?> clazz = obj.getClass();
        Map<Integer, Method> m = getMethods(clazz);
        while (!m.isEmpty()) {
            methods.putAll(m);
            clazz = clazz.getSuperclass();
            m = getMethods(clazz);
        }
    }

    private Map<Integer, Method> getMethods(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(e -> e.isAnnotationPresent(MessageRoute.class))
                .collect(Collectors.toMap(e ->  e.getAnnotation(MessageRoute.class).value(), e -> e));
    }

    public void execute(UserSession userSession, int type, JsonObject data) throws InvocationTargetException, IllegalAccessException {
        if (!methods.containsKey(type)) {
            log.warn("Method not found for type {}", type);
            return;
        }
        Method method = methods.get(type);
        method.setAccessible(true);
        method.invoke(source, userSession, data);
    }
}
