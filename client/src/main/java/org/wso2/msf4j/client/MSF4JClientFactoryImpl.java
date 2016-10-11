/*
* Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.msf4j.client;

import feign.hystrix.FallbackFactory;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.UUID;

public class MSF4JClientFactoryImpl extends BasePooledObjectFactory<MSF4JClient> {

    private Class apiInterface;
    private String instanceName;
    private String endpoint;
    private String analyticsEndpoint;
    private boolean enableCircuitBreaker;
    private boolean enableTracing;
    private FallbackFactory fallbackFactory;

    public MSF4JClientFactoryImpl(Class apiInterface, String instanceName, String endpoint, String analyticsEndpoint,
                                  boolean enableCircuitBreaker, boolean enableTracing, FallbackFactory fallbackFactory) {
        this.apiInterface = apiInterface;
        this.instanceName = instanceName;
        this.endpoint = endpoint;
        this.analyticsEndpoint = analyticsEndpoint;
        this.enableCircuitBreaker = enableCircuitBreaker;
        this.enableTracing = enableTracing;
        this.fallbackFactory = fallbackFactory;
    }

    @Override
    public MSF4JClient create() throws Exception {
        return new MSF4JClient.Builder<>().analyticsEndpoint(analyticsEndpoint)
                                          .apiClass(apiInterface)
                                          .enableCircuitBreaker(enableCircuitBreaker)
                                          .enableTracing(enableTracing)
                                          .instanceName(instanceName)
                                          .serviceEndpoint(endpoint)
                                          .fallbackFactory(fallbackFactory)
                                          .build();
    }

    @Override
    public PooledObject<MSF4JClient> wrap(MSF4JClient tmsf4JClient) {
        return new DefaultPooledObject<>(tmsf4JClient);
    }
}