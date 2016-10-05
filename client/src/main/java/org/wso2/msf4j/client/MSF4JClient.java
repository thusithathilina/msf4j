/*
 *  Copyright (c) 2016 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
 */
package org.wso2.msf4j.client;

import feign.Client;
import feign.Feign;
import feign.RequestInterceptor;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.hystrix.FallbackFactory;
import feign.hystrix.HystrixFeign;
import org.wso2.msf4j.client.codec.ServiceExceptionErrorDecoder;

import java.util.ArrayList;
import java.util.List;

/**
 * MSF4J client.
 *
 * @param <T> REST service API interface
 */
public class MSF4JClient<T> {

    private final T api;

    public MSF4JClient(T api) {
        this.api = api;
    }

    public T api() {
        return api;
    }

    /**
     * MSF4J client builder
     *
     * @param <T> API interface
     */
    public static class Builder<T> {

        private final List<RequestInterceptor> requestInterceptors = new ArrayList<RequestInterceptor>();
        private FallbackFactory<? extends T> fallbackFactory;
        private boolean enableCircuitBreaker;
        private boolean enableTracing;
        /*private boolean enableHttps;
        private String truststorePath;
        private String truststoreKey;*/
        private String instanceName;
        private String analyticsEndpoint;
        private String serviceEndpoint;
        private Class<T> apiClass;

        public Feign.Builder newFeignClientBuilder() {
            return Feign.builder()
                    .encoder(new GsonEncoder(ModelUtils.GSON))
                    .decoder(new GsonDecoder(ModelUtils.GSON))
                    .errorDecoder(new ServiceExceptionErrorDecoder(apiClass));
        }

        public HystrixFeign.Builder newHystrixFeignClientBuilder() {
            return HystrixFeign.builder()
                    .encoder(new GsonEncoder(ModelUtils.GSON))
                    .decoder(new GsonDecoder(ModelUtils.GSON))
                    .errorDecoder(new ServiceExceptionErrorDecoder(apiClass));
        }

        /**
         * Adds a single request interceptor to the builder.
         */
        public MSF4JClient.Builder<T> requestInterceptor(RequestInterceptor requestInterceptor) {
            this.requestInterceptors.add(requestInterceptor);
            return this;
        }

        /**
         * Sets the full set of request interceptors for the builder, overwriting any previous
         * interceptors.
         */
        public MSF4JClient.Builder<T> requestInterceptors(Iterable<RequestInterceptor> requestInterceptors) {
            this.requestInterceptors.clear();
            for (RequestInterceptor requestInterceptor : requestInterceptors) {
                this.requestInterceptors.add(requestInterceptor);
            }
            return this;
        }

        /**
         * Sets the fallback factory for HystrixFeign client which supports circuit breaker
         */
        public MSF4JClient.Builder<T> fallbackFactory(FallbackFactory<? extends T> fallbackFactory) {
            this.fallbackFactory = fallbackFactory;
            return this;
        }

        public MSF4JClient.Builder<T> enableCircuitBreaker() {
            this.enableCircuitBreaker = true;
            return this;
        }

        public MSF4JClient.Builder<T> enableTracing() {
            this.enableTracing = true;
            return this;
        }

      /*  public MSF4JClient.Builder<T> enableHttps() {
            this.enableHttps = true;
            return this;
        }

        public MSF4JClient.Builder<T> truststorePath(String truststorePath) {
            this.truststorePath = truststorePath;
            return this;
        }

        public MSF4JClient.Builder<T> truststoreKey(String truststoreKey) {
            this.truststoreKey = truststoreKey;
            return this;
        }*/

        public MSF4JClient.Builder<T> instanceName(String instanceName) {
            this.instanceName = instanceName;
            return this;
        }

        public MSF4JClient.Builder<T> analyticsEndpoint(String analyticsEndpoint) {
            this.analyticsEndpoint = analyticsEndpoint;
            return this;
        }

        public MSF4JClient.Builder<T> serviceEndpoint(String serviceEndpoint) {
            this.serviceEndpoint = serviceEndpoint;
            return this;
        }

        public MSF4JClient.Builder<T> apiClass(Class<T> apiClass) {
            this.apiClass = apiClass;
            return this;
        }

        public MSF4JClient<T> build() {
            MSF4JClient<T> msf4JClient;
            Client client;
            if (enableTracing) {
                client = new FeignClientWrapper(new FeignTracingClient(new ApacheHttpClient(),
                        instanceName, analyticsEndpoint));
            } else {
                client = new FeignClientWrapper(new ApacheHttpClient());
            }

            if (enableCircuitBreaker) {
                HystrixFeign.Builder builder = newHystrixFeignClientBuilder();
                builder.client(client);
                builder.requestInterceptors(requestInterceptors);
                msf4JClient = new MSF4JClient<T>(builder.target(apiClass, serviceEndpoint, fallbackFactory));
            } else {
                Feign.Builder builder = newFeignClientBuilder();
                builder.client(client);
                builder.requestInterceptors(requestInterceptors);
                msf4JClient = new MSF4JClient<T>(builder.target(apiClass, serviceEndpoint));
            }
            return msf4JClient;
        }
    }
}
