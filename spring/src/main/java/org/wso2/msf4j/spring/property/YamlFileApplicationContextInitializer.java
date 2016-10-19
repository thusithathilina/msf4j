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
package org.wso2.msf4j.spring.property;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;

/**
 * ApplicationContextInitializer class used to load environment properties from YAML files.
 */
public class YamlFileApplicationContextInitializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

        try {
            Resource resource = applicationContext.getResource("classpath:application1.yml");
            if (!resource.exists()) {
                resource = applicationContext.getResource("file:application1.yml");

            }
            if (resource.exists()) {
                YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
                yaml.setResources(resource);
                PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer =
                        new PropertySourcesPlaceholderConfigurer();
                propertySourcesPlaceholderConfigurer.setProperties(yaml.getObject());

                propertySourcesPlaceholderConfigurer.postProcessBeanFactory(applicationContext.getBeanFactory());
                PropertySource<?> propertySource = propertySourcesPlaceholderConfigurer.getAppliedPropertySources()
                                                                                       .get(PropertySourcesPlaceholderConfigurer.LOCAL_PROPERTIES_PROPERTY_SOURCE_NAME);

                applicationContext.getEnvironment().getPropertySources().addFirst(propertySource);
                //applicationContext.refresh();

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
