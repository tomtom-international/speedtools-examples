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

package com.tomtom.examples.exampleUsingDatabase;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.mongodb.Mongo;
import com.tomtom.examples.exampleUsingDatabase.dao.PersonDao;
import com.tomtom.examples.exampleUsingDatabase.dao.mongodb.PersonDaoMongoDBImpl;
import com.tomtom.examples.exampleUsingDatabase.dao.mongodb.mappers.MyMapperRegistry;
import com.tomtom.speedtools.mongodb.MongoConnectionCache;
import com.tomtom.speedtools.mongodb.MongoDB;
import com.tomtom.speedtools.mongodb.MongoDBConnectionException;
import com.tomtom.speedtools.mongodb.mappers.MapperRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.net.UnknownHostException;

/**
 * This is an example Guice module of how to bind service implementations to their interfaces. This examples binds a
 * "version 1" and "version 2" interfaces of a REST API to 2 different implementations. It also binds a rudimentary
 * database implementation to a database interface.
 */
public class ExampleDatabaseModule implements Module {
    private static final Logger LOG = LoggerFactory.getLogger(ExampleDatabaseModule.class);

    public ExampleDatabaseModule() {
        super();
    }

    @Override
    public void configure(@Nonnull final Binder binder) {
        assert binder != null;

        // Bind APIs to their implementation.
        binder.bind(ExampleDatabaseResource.class).to(ExampleDatabaseResourceImpl.class).in(Singleton.class);

        // Bind DAOs.
        binder.bind(PersonDao.class).to(PersonDaoMongoDBImpl.class).in(Singleton.class);

        // Bind mapper registry.
        binder.bind(MapperRegistry.class).to(MyMapperRegistry.class).in(Singleton.class);

        /**
         * Show some start-up information about this example application.
         */
        LOG.info("configure:");
        LOG.info("configure: GET /example/3/x       Same as /example/1/x using MongoDB");
    }

    @Nonnull
    @Provides
    @Singleton
    public MongoDB provideMongoDB() {

        try {
            final Mongo mongo = MongoConnectionCache.getMongoDB("localhost:27017", 5000, "example", "", "");
            return getDB(mongo, "example", "");
        } catch (final UnknownHostException e) {
            throw new MongoDBConnectionException("Cannot find MongoDB servers", e);
        }
    }

    /**
     * Get the MongoDB database instance.
     *
     * @param mongo           MongoDB handle.
     * @param databaseName    Database name.
     * @param subDatabaseName Sub-database name (for unit tests). If the subdatabase name is empty, the database name is
     *                        used, otherwise the subdatabase name is appended to. Example, database="TEST", subdatabase
     *                        name="texas" would produce "TEST_texas".
     * @return MongoDB instance.
     * @throws MongoDBConnectionException If something went wrong.
     */
    @Nonnull
    protected MongoDB getDB(@Nonnull final Mongo mongo, @Nonnull final String databaseName,
                            @Nonnull final String subDatabaseName) {
        assert mongo != null;
        assert databaseName != null;
        assert subDatabaseName != null;

        final MongoDB mongoDb = new MongoDB(mongo.getDB(databaseName), subDatabaseName);
        return mongoDb;
    }
}
