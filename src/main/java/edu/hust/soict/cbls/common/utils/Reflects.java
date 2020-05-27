package edu.hust.soict.cbls.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Các helper method làm việc với Reflections
 *
 *
 */
@SuppressWarnings("unchecked")
public class Reflects {

    private static final ClassLoader mainCl = Reflects.class.getClassLoader();
    private static final Logger logger = LoggerFactory.getLogger(Reflects.class);

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClassInstance(T object) {
        return (Class<T>) object.getClass();
    }

    public static <T> T newInstance(Class<T> c) {
        try {
            return c.newInstance();
        } catch (IllegalAccessException | InstantiationException | NullPointerException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T newInstance(String className) {
        try {
            return (T) mainCl.loadClass(className).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T newInstance(Class<T> clazz, Class<?>[] parameterTypes, Object... parameters) {
        try {
            return clazz.getConstructor(parameterTypes).newInstance(parameters);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                | NoSuchMethodException e) {
            logger.error("Error while create new instance using java reflection", e);
            throw new RuntimeException(e);
        }
    }

    public static <T> T newInstance(String className, Class<?>[] parameterTypes, Object... parameters) {
        try {
            return (T) mainCl.loadClass(className).getConstructor(parameterTypes).newInstance(parameters);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                | NoSuchMethodException | ClassNotFoundException e) {
            logger.error("Error while create new instance using java reflection", e);
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> newInstancesWithDefaultConstructor(Collection<String> classNames){
        List<T> instances = new LinkedList<>();
        for(String className : classNames){
            instances.add(newInstance(className));
        }

        return instances;
    }
}
