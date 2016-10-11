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

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class MSF4JClientPool<T> extends GenericObjectPool<MSF4JClient<T>> {

    public MSF4JClientPool(PooledObjectFactory<MSF4JClient<T>> factory) {
        super(factory);
    }

    public MSF4JClientPool(PooledObjectFactory<MSF4JClient<T>> factory, GenericObjectPoolConfig config) {
        super(factory, config);
    }
}