package org.wso2.msf4j.client.codec;

import org.wso2.msf4j.client.exception.RestException;
import org.wso2.msf4j.client.model.ErrorCodeAndMessage;

/**
 * Feign error decoder which translates REST service error response represented by {@link ErrorCodeAndMessage} to
 * Java exception class which inherits {@link RestException}
 */
public class ServiceExceptionErrorDecoder extends ReflectionErrorDecoder<ErrorCodeAndMessage, RestException> {

    public ServiceExceptionErrorDecoder(Class<?> apiClass) {
        super(apiClass, ErrorCodeAndMessage.class, RestException.class);
    }

    @Override
    protected String getKeyFromException(RestException exception) {
        return exception.getErrorCode();
    }

    @Override
    protected String getKeyFromResponse(ErrorCodeAndMessage apiResponse) {
        return apiResponse.getErrorCode();
    }

    @Override
    protected String getMessageFromResponse(ErrorCodeAndMessage apiResponse) {
        return apiResponse.getMessage();
    }
}
