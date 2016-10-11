/*
 * Copyright (c) 2016, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.msf4j.example.service;

import feign.FeignException;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.msf4j.client.MSF4JClientFactory;
import org.wso2.msf4j.client.MSF4JClient;
import org.wso2.msf4j.client.MSF4JClientFactoryImpl;
import org.wso2.msf4j.client.exception.RestException;
import org.wso2.msf4j.example.client.api.CustomerServiceAPI;
import org.wso2.msf4j.example.client.api.InvoiceServiceAPI;
import org.wso2.msf4j.example.client.exception.CustomerNotFoundRestException;
import org.wso2.msf4j.example.client.exception.InvoiceNotFoundRestException;
import org.wso2.msf4j.example.exception.CustomerNotFoundException;
import org.wso2.msf4j.example.exception.InvoiceNotFoundException;
import org.wso2.msf4j.example.exception.ServerErrorException;
import org.wso2.msf4j.example.model.Customer;
import org.wso2.msf4j.example.model.Invoice;
import org.wso2.msf4j.example.model.InvoiceReport;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * ReportService resource class.
 */
@Path("/report")
public class ReportService {

    private static final Logger log = LoggerFactory.getLogger(ReportService.class);
    private static final String CUSTOMER_SERVICE_URL = "http://localhost:8081";
    private static final String INVOICE_SERVICE_URL = "http://localhost:8082";
    private static final String DAS_RECEIVER_URL = "http://localhost:9763/endpoints/msf4jtracereceiver";

    private final Map<String, Customer> customerCachedMap = new HashMap<>();
    private final Map<String, Invoice> invoiceCachedMap = new HashMap<>();
    private final MSF4JClient<CustomerServiceAPI> customerServiceClient;
    private final MSF4JClient<InvoiceServiceAPI> invoiceServiceClient;

    public ReportService() throws Exception {
        FallbackFactory<CustomerServiceAPI> customerServiceFallback = cause -> (id) -> {
            if (cause instanceof FeignException) {
                return customerCachedMap.get(id);
            }
            throw (RestException) cause;
        };

        FallbackFactory<InvoiceServiceAPI> invoiceServiceFallback = cause -> (id) -> {
            if (cause instanceof FeignException) {
                return invoiceCachedMap.get(id);
            }
            throw (RestException) cause;
        };

        /*customerServiceClient = new MSF4JClient.Builder<CustomerServiceAPI>()
                .analyticsEndpoint(DAS_RECEIVER_URL)
                .apiClass(CustomerServiceAPI.class)
                //.enableCircuitBreaker()
                //.enableTracing()
                .instanceName("CustomerServiceClient")
                .serviceEndpoint(CUSTOMER_SERVICE_URL)
                .fallbackFactory(customerServiceFallback)
                .build();*/
        MSF4JClientFactory msf4JClientFactory = new MSF4JClientFactory();
        customerServiceClient = msf4JClientFactory.createClient(
                new MSF4JClientFactoryImpl(CustomerServiceAPI.class, "CustomerServiceClient", CUSTOMER_SERVICE_URL,
                                           DAS_RECEIVER_URL, false, false, customerServiceFallback));
        /*customerServiceClient = msf4JClientFactory
                .createClient(CustomerServiceAPI.class, "CustomerServiceClient", CUSTOMER_SERVICE_URL,
                              customerServiceFallback, DAS_RECEIVER_URL, false, false);*/

        invoiceServiceClient = msf4JClientFactory.createClient(
                new MSF4JClientFactoryImpl(InvoiceServiceAPI.class, "InvoiceServiceClient", INVOICE_SERVICE_URL,
                                           DAS_RECEIVER_URL, false, false, invoiceServiceFallback));
        /*invoiceServiceClient = msf4JClientFactory
                .createClient(InvoiceServiceAPI.class, "InvoiceServiceClient", INVOICE_SERVICE_URL,
                              invoiceServiceFallback, DAS_RECEIVER_URL, false, false);*/

        /*invoiceServiceClient = new MSF4JClient.Builder<InvoiceServiceAPI>()
                .analyticsEndpoint(DAS_RECEIVER_URL)
                .apiClass(InvoiceServiceAPI.class)
                .enableCircuitBreaker()
                .enableTracing()
                .instanceName("InvoiceServiceClient")
                .serviceEndpoint(INVOICE_SERVICE_URL)
                .fallbackFactory(invoiceServiceFallback)
                .build();*/

    }

    /**
     * Retrieves the invoice report for a given invoice ID.
     * http://localhost:8080/report/invoice/I001
     *
     * @param id Invoice ID will be taken from the path parameter.
     * @return
     */
    @GET
    @Path("/invoice/{id}")
    @Produces({"application/json"})
    public Response getInvoiceReport(@PathParam("id") String id) throws InvoiceNotFoundException,
            CustomerNotFoundException, ServerErrorException {
        InvoiceReport invoiceReport;
        Invoice invoice;
        try {
            invoice = invoiceServiceClient.api().getInvoice(id);
            if (log.isDebugEnabled()) {
                log.info("Invoice retrieved: " + invoice);
            }
            invoiceCachedMap.put(invoice.getId(), invoice);
        } catch (InvoiceNotFoundRestException e) {
            throw new InvoiceNotFoundException(e);
        } catch (RestException | FeignException e) {
            throw new ServerErrorException();
        }

        try {
            String customerId = invoice.getCustomerId();
            Customer customer = customerServiceClient.api().getCustomer(customerId);
            if (log.isDebugEnabled()) {
                log.debug("Customer retrieved: " + customer);
            }
            customerCachedMap.put(customerId, customer);
            invoiceReport = new InvoiceReport(invoice, customer);
        } catch (CustomerNotFoundRestException e) {
            throw new CustomerNotFoundException(e);
        } catch (RestException | FeignException e) {
            throw new ServerErrorException();
        }

        return Response.status(Response.Status.OK).entity(invoiceReport).build();
    }
}
