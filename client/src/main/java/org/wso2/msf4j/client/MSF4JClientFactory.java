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

import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.hystrix.HystrixFeign;

/**
 * MSF4J client.
 */
public class MSF4JClientFactory {

    public static <T> Feign.Builder newFeignClientBuilder() {
        return Feign.builder()
                .encoder(new GsonEncoder(ModelUtils.GSON))
                .decoder(new GsonDecoder(ModelUtils.GSON))
                .errorDecoder(new ClientErrorDecoder());
    }

    public static <T> T newFeignClientInstance(Class<T> apiClass, String serviceEndpoint) {
        return newFeignClientBuilder().target(apiClass, serviceEndpoint);
    }

    public static <T> T newFeignTracingClientInstance(Class<T> apiClass, String serviceEndpoint, String
            analyticsEndpoint, String instanceName) {
        return newFeignClientBuilder()
                .client(new FeignTracingClient(instanceName, analyticsEndpoint))
                .target(apiClass, serviceEndpoint);
    }

    public static <T> HystrixFeign.Builder newHystrixFeignClientBuilder() {
        return HystrixFeign.builder()
                .encoder(new GsonEncoder(ModelUtils.GSON))
                .decoder(new GsonDecoder(ModelUtils.GSON))
                .errorDecoder(new ClientErrorDecoder());
    }

    public static <T> T newHystrixFeignClientInstance(Class<T> apiClass, String serviceEndpoint, T fallbackFactory) {
        return newHystrixFeignClientBuilder()
                .target(apiClass, serviceEndpoint, fallbackFactory);
    }

    public static <T> T newHystrixFeignTracingClientInstance(Class<T> apiClass, String serviceEndpoint, T
            fallbackFactory, String analyticsEndpoint, String instanceName) {
        return newHystrixFeignClientBuilder()
                .client(new FeignTracingClient(instanceName, analyticsEndpoint))
                .target(apiClass, serviceEndpoint, fallbackFactory);
    }

}
