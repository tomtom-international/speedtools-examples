/*
 * Copyright (C) 2012-2019, TomTom (http://tomtom.com).
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

package com.tomtom.examples.exampleCreatingScalableServices;

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
public class ExampleServicesModule implements Module {
    private static final Logger LOG = LoggerFactory.getLogger(ExampleServicesModule.class);

    @Override
    public void configure(@Nonnull final Binder binder) {
        assert binder != null;

        // Bind APIs to their implementation.
        binder.bind(FutureBasedResource.class).to(FutureBasedResourceImpl.class).in(Singleton.class);
        binder.bind(SimpleThreadBasedResource.class).to(SimpleThreadBasedResourceImpl.class).in(Singleton.class);
        binder.bind(SimulatedBusyDatabase.class).in(Singleton.class);

        /**
         * Show some start-up information about this example application.
         */
        LOG.info("configure:");
        LOG.info("configure: Services /example/1 and /example/2 show how you can create REST API calls without");
        LOG.info("configure:        and with the SpeedTools Akka framework for high scalability");
        LOG.info("configure:");
        LOG.info("configure: GET    /example/[1|2]/person               Get list of person IDs");
        LOG.info("configure: GET    /example/[1|2]/person/<personId>    Get specific person");
        LOG.info("configure: POST   /example/[1|2]/person               Create new person");
        LOG.info("configure: DELETE /example/[1|2]/person/<personId>    Delete person");
        LOG.info("configure:");
        LOG.info("configure: GET    /example/2/version                  Get POM version number");
    }
}
