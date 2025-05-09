package io.github.tfkfan.orbital.core.route;

import io.github.tfkfan.orbital.core.session.Session;
import io.github.tfkfan.orbital.core.session.GatewaySession;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class RouteProcessor {
    private final Map<Integer, Method> methods;
    private final Object source;

    public RouteProcessor(Object source) {
        this.source = source;
        methods = new HashMap<>();
        Class<?> clazz = source.getClass();
        Map<Integer, Method> m = getMethods(clazz);
        while (!clazz.equals(Object.class)) {
            methods.putAll(m);
            clazz = clazz.getSuperclass();
            m = getMethods(clazz);
        }
    }

    private Map<Integer, Method> getMethods(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(e -> e.isAnnotationPresent(MessageRoute.class) && !methods.containsKey(e.getAnnotation(MessageRoute.class).value()))
                .collect(Collectors.toMap(e -> e.getAnnotation(MessageRoute.class).value(), e -> e));
    }

    public void execute(Session userSession, int type, JsonObject data) throws InvocationTargetException, IllegalAccessException {
        if (!methods.containsKey(type)) {
            log.warn("Method not found for type {}", type);
            return;
        }

        final Method method = methods.get(type);
        method.setAccessible(true);

        final Object[] args = Arrays.stream(method.getParameters()).map(it -> {
            if (it.getType().equals(GatewaySession.class))
                return userSession;
            if (it.getType().equals(JsonObject.class)) {
                if (data == null)
                    return new JsonObject();
                return data;
            }
            return null;
        }).toArray(Object[]::new);

        log.trace("Method {} invoked with args: {}", method.getName(), Arrays.toString(args));

        method.invoke(source, args);
    }
}
