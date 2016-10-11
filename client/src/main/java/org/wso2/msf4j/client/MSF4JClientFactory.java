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

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class MSF4JClientFactory<T> {
    private MSF4JClientPool<MSF4JClient<T>> clientObjectPool;

   /* public MSF4JClient createClient(Class apiInterface, String instanceName, String endpoint,
                                    FallbackFactory fallbackFactory, String analyticsEndpoint,
                                    boolean enableCircuitBreaket, boolean enableTracing) throws Exception {
        if (clientObjectPool == null) {
            GenericObjectPoolConfig config = new GenericObjectPoolConfig();
            config.setMaxIdle(1);
            config.setMaxTotal(1);
            config.setTestOnBorrow(true);
            config.setTestOnReturn(true);
            clientObjectPool = new MSF4JClientPool(
                    new MSF4JClientFactoryImpl(apiInterface, instanceName, endpoint, analyticsEndpoint,
                                               enableCircuitBreaket, enableTracing, fallbackFactory), config);
        }

        return clientObjectPool.borrowObject();

    }*/

    public MSF4JClient createClient(MSF4JClientFactoryImpl msf4JClientFactoryImpl, GenericObjectPoolConfig config)
            throws Exception {

        clientObjectPool = new MSF4JClientPool(msf4JClientFactoryImpl, config);
        for (int i = 0; i < config.getMaxTotal(); i++) {
            clientObjectPool.addObject();
        }
        return clientObjectPool.borrowObject();
    }

    public MSF4JClient createClient(MSF4JClientFactoryImpl msf4JClientFactoryImpl) throws Exception {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxIdle(10);
        config.setMaxTotal(10);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        return createClient(msf4JClientFactoryImpl, config);
    }

}
