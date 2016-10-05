package org.wso2.msf4j.client.codec;

import java.lang.reflect.InvocationTargetException;

/**
 * A bean class that stores information about exceptions being thrown by REST service
 * @param <T> Exception class being thrown by REST service
 */
public class ThrownExceptionDetails<T> {
    private Class<? extends T> clazz;
    private ExceptionSupplier<T> exceptionSupplier;

    public ThrownExceptionDetails(Class<? extends T> clazz, ExceptionSupplier<T> exceptionSupplier) {
        this.clazz = clazz;
        this.exceptionSupplier = exceptionSupplier;
    }

    public Class<? extends T> getClazz() {
        return clazz;
    }

    public ExceptionSupplier<T> getServiceExceptionSupplier() {
        return exceptionSupplier;
    }

    public T instantiate()
            throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        return exceptionSupplier.get();
    }
}
