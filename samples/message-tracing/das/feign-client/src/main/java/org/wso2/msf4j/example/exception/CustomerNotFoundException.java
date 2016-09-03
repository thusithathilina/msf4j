/*
 *  Copyright (c) 2005-2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 *//*    new MicroservicesRunner(8082)
                .addExceptionMapper(new EntityNotFoundMapper(), new InvoiceNotFoundMapper())
                .addInterceptor(new MSF4JTracingInterceptor("Invoice-Service"))
                .deploy(new InvoiceService())
                .start();

        new MicroservicesRunner(8080)
                .addExceptionMapper(new EntityNotFoundMapper(), new CustomerNotFoundMapper(), new
                        InvoiceNotFoundMapper())
                .addInterceptor(new MSF4JTracingInterceptor("Report-Service"))
                .deploy(new ReportService())
                .start();*/
package org.wso2.msf4j.example.exception;

/**
 * Thrown when a Symbol is not found.
 */
public class CustomerNotFoundException extends EntityNotFoundException {
    public CustomerNotFoundException() {
        super();
    }

    public CustomerNotFoundException(String message) {
        super(message);
    }

    public CustomerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomerNotFoundException(Throwable cause) {
        super(cause);
    }

    protected CustomerNotFoundException(String message, Throwable cause,
                                        boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
