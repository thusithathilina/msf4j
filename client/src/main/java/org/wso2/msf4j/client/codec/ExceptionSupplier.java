package org.wso2.msf4j.client.codec;

import java.lang.reflect.InvocationTargetException;

/**
 * Interface for instantiating exceptions
 *
 * @param <S> Exception class to be instantiated
 */
@FunctionalInterface
public interface ExceptionSupplier<S> {
    S get()
            throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException;
}
