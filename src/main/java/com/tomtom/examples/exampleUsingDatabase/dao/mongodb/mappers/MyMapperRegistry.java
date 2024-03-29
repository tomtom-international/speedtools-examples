/*
 * Copyright (C) 2012-2021, TomTom (http://tomtom.com).
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *     http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tomtom.examples.exampleUsingDatabase.dao.mongodb.mappers;


import com.tomtom.speedtools.mongodb.mappers.MapperRegistry;
import com.tomtom.speedtools.mongodb.mappers.SchemaException;

/**
 * An application needs to register all data mappers in a registry. In this example the registry explicitly registers
 * all mappers from the utils library as well as our own PersonMapper.
 */
public class MyMapperRegistry extends MapperRegistry {

    public MyMapperRegistry() throws SchemaException {
        super();
        register(
                new PersonMapper()
        );
    }
}
