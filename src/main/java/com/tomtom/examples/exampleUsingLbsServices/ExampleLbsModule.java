/*
 *  Copyright (C) 2012-2016. TomTom International BV (http://tomtom.com).
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

package com.tomtom.examples.exampleUsingLbsServices;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

/**
 * This is an example Guice module of how to bind service implementations to their interfaces. This examples binds a
 * "version 1" and "version 2" interfaces of a REST API to 2 different implementations. It also binds a rudimentary
 * database implementation to a database interface.
 */
public class ExampleLbsModule implements Module {
    private static final Logger LOG = LoggerFactory.getLogger(ExampleLbsModule.class);

    @Override
    public void configure(@Nonnull final Binder binder) {
        assert binder != null;

        // Bind APIs to their implementation.
        binder.bind(ExampleLbsResource.class).to(ExampleLbsResourceImpl.class).in(Singleton.class);

        /**
         * Show some start-up information about this example application.
         */
        LOG.info("configure:");
        LOG.info("configure: GET /lbs/traceme             Issue a trace event to MongoDB");
        LOG.info("configure: GET /lbs/geocode/<query>     Perform geo-coding request");
        LOG.info("configure: GET /lbs/route/<from>/<to>   Calculate route");
    }
}
