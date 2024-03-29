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

package com.tomtom.examples.deployment;

import com.google.inject.Binder;
import com.tomtom.speedtools.guice.GuiceConfigurationModule;
import com.tomtom.speedtools.tracer.LoggingTraceHandler;
import com.tomtom.speedtools.tracer.TracerFactory;
import com.tomtom.speedtools.tracer.mongo.MongoDBTraceHandler;
import com.tomtom.speedtools.tracer.mongo.MongoDBTraceProperties;
import com.tomtom.speedtools.tracer.mongo.MongoDBTraceStream;

import javax.annotation.Nonnull;


/**
 * This example class defines the deployment configuration for Google Guice.
 *
 * The deployment module "bootstraps" the whole Guice injection process.
 *
 * It bootstraps the Guice injection and specifies the property files to be read. It also needs to bind the tracer, so
 * they can be used early on in the app. Finally, it can bind a "startup check" (example provided) as an eager
 * singleton, so the system won't start unless a set of basic preconditions are fulfilled.
 *
 * The "speedtools.default.properties" is required, but its values may be overridden in other property files.
 */
public class DeploymentModule extends GuiceConfigurationModule {

    public DeploymentModule() {
        super(
                "classpath:speedtools.default.properties",  // Default set of properties required by SpeedTools.
                "classpath:example.properties");            // Additional property file(s).
    }

    @Override
    public void configure(@Nonnull final Binder binder) {
        assert binder != null;

        super.configure(binder);

        /**
         * Note: Do not bind your REST API service implementations in this module. Use separate
         * modules for those, like the one in the "exampleXYZ" example directories. That keeps
         * the organization of modules much cleaner.
         */

        // Bind these classes if you wish to use the SpeedTools MongoDB tracing framework.
        TracerFactory.setEnabled(true);
        binder.bind(MongoDBTraceProperties.class).asEagerSingleton();
        binder.bind(MongoDBTraceStream.class);
        binder.bind(MongoDBTraceHandler.class).asEagerSingleton();
        binder.bind(LoggingTraceHandler.class).asEagerSingleton();

        // Bind start-up checking class (example).
        binder.bind(StartupCheck.class).asEagerSingleton();
    }
}
