/*
 *  Copyright (C) 2015. TomTom International BV (http://tomtom.com).
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.tomtom.examples.exampleUsingDatabase.dao.mongodb.mappers;


import com.tomtom.speedtools.mongodb.mappers.BooleanMapper;
import com.tomtom.speedtools.mongodb.mappers.CurrencyMapper;
import com.tomtom.speedtools.mongodb.mappers.DateTimeMapper;
import com.tomtom.speedtools.mongodb.mappers.DoubleMapper;
import com.tomtom.speedtools.mongodb.mappers.GeoAreaMapper;
import com.tomtom.speedtools.mongodb.mappers.GeoPointMapper;
import com.tomtom.speedtools.mongodb.mappers.GeoRectangleMapper;
import com.tomtom.speedtools.mongodb.mappers.GpsTraceMapper;
import com.tomtom.speedtools.mongodb.mappers.GpsTracePointMapper;
import com.tomtom.speedtools.mongodb.mappers.IntegerMapper;
import com.tomtom.speedtools.mongodb.mappers.LocaleMapper;
import com.tomtom.speedtools.mongodb.mappers.LongMapper;
import com.tomtom.speedtools.mongodb.mappers.MapperRegistry;
import com.tomtom.speedtools.mongodb.mappers.MoneyMapper;
import com.tomtom.speedtools.mongodb.mappers.SchemaException;
import com.tomtom.speedtools.mongodb.mappers.StringMapper;
import com.tomtom.speedtools.mongodb.mappers.URLMapper;

/**
 * An application needs to register all data mappers in a registry. In this example the registry explicitly registers
 * all mappers from the utils library as well as our own PersonMapper.
 */
public class MyMapperRegistry extends MapperRegistry {

    public MyMapperRegistry() throws SchemaException {
        super();
        register(
                /**
                 * From module 'utils':
                 */
                new BooleanMapper(),
                new CurrencyMapper(),
                new DateTimeMapper(),
                new DoubleMapper(),
                new GeoAreaMapper(),
                new GeoPointMapper(),
                new GeoRectangleMapper(),
                new GpsTraceMapper(),
                new GpsTracePointMapper(),
                new IntegerMapper(),
                new LocaleMapper(),
                new LongMapper(),
                new MoneyMapper(),
                new StringMapper(),
                new URLMapper(),

                /**
                 * From module 'dao':
                 */
                new PersonMapper()
        );
    }
}
