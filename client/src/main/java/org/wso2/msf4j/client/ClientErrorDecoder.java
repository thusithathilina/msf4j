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

import feign.Response;
import feign.codec.ErrorDecoder;
import org.wso2.msf4j.client.exception.ClientException;
import org.wso2.msf4j.client.exception.ResourceNotFoundException;
import org.wso2.msf4j.client.exception.ServerErrorException;

public class ClientErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404) {
            return new ResourceNotFoundException(methodKey, response);
        } else if (response.status() >= 500 && response.status() < 511) {
            return new ServerErrorException(methodKey, response);
        }
        return new ClientException(methodKey, response);
    }
}
