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

import akka.dispatch.Futures;
import org.jboss.resteasy.annotations.Suspend;
import org.jboss.resteasy.spi.AsynchronousResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.Future;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import com.tomtom.examples.ApiConstants;
import com.tomtom.examples.exampleCreatingScalableServices.binders.PersonBinder;
import com.tomtom.examples.exampleCreatingScalableServices.converters.PersonConverter;
import com.tomtom.examples.exampleCreatingScalableServices.domain.Person;
import com.tomtom.examples.exampleUsingDatabase.dao.PersonDao;
import com.tomtom.speedtools.apivalidation.exceptions.ApiInvalidParameterCombinationException;
import com.tomtom.speedtools.apivalidation.exceptions.ApiNotFoundException;
import com.tomtom.speedtools.apivalidation.exceptions.ApiParameterMissingException;
import com.tomtom.speedtools.apivalidation.exceptions.ApiUidSyntaxException;
import com.tomtom.speedtools.domain.Uid;
import com.tomtom.speedtools.mongodb.EntityStoreException;
import com.tomtom.speedtools.rest.ResourceHandler;
import com.tomtom.speedtools.rest.ResourceProcessor;

import static com.tomtom.speedtools.loghelper.LogHelper.logId;

/**
 * This class implements a REST API resource using the SpeedTools framework and includes a simple "tracer" which adds a
 * trace entry to the tracer MongoDB database.
 *
 * The example also show (in a very simplistic way) how you can use the logId() call to display IDs or keys in a log
 * file together with a human readable name. The method logId() keeps a limited size cache to map IDs to name. It is
 * often much more readable to see something like "Mr. Jones[cb17ffb0-adcf-11e3-a5e2-0800200c9a66]" than just the UUID
 * itself.
 */
@SuppressWarnings({"OverlyBroadThrowsClause", "ProhibitedExceptionDeclared"})
public class ExampleDatabaseResourceImpl implements ExampleDatabaseResource {
    private static final Logger LOG = LoggerFactory.getLogger(ExampleDatabaseResourceImpl.class);

    @Nonnull
    private final ResourceProcessor processor;
    @Nonnull
    private final PersonDao personDao;

    @Inject
    public ExampleDatabaseResourceImpl(
            @Nonnull final ResourceProcessor processor,
            @Nonnull final PersonDao personDao) {
        assert processor != null;
        assert personDao != null;

        // Store the injected values.
        this.processor = processor;
        this.personDao = personDao;

        // Create a sample data store with entries. Note that this class is a singleton.
        try {
            personDao.storePerson(new Person(Uid.fromString("1-1-1-1-1").as(Person.class), "User 1", 30));
            personDao.storePerson(new Person(Uid.fromString("1-1-1-1-2").as(Person.class), "User 2", 42));
        } catch (final EntityStoreException ignored) {
            // Ignore.
        }
    }


    @Override
    public void getPersons(
            @Nonnull @Suspend(ApiConstants.SUSPEND_TIMEOUT) final AsynchronousResponse response) {
        assert response != null;

        processor.process(LOG, response, new ResourceHandler("getPersons(2)") {

            @Nonnull
            @Override
            public Future<?> process() throws Exception {

                final List<Person> list = personDao.getPersons();
                final List<PersonBinder> result = new ArrayList<PersonBinder>();
                for (final Person p : list) {
                    final PersonBinder binder = PersonConverter.fromDomain(p);
                    binder.validate();
                    result.add(binder);
                }
                response.setResponse(Response.ok(result).build());

                // The response is already set within this method body.
                return Futures.successful(null);
            }
        });
    }

    @Override
    public void getPerson(
            @Nonnull @PathParam(PARAM_PERSON_ID) final String personId,
            @Nonnull @Suspend(ApiConstants.SUSPEND_TIMEOUT) final AsynchronousResponse response) {
        assert personId != null;
        assert response != null;

        processor.process(LOG, response, new ResourceHandler("getPerson(2)") {

            @Nonnull
            @Override
            public Future<?> process() throws Exception {

                // Check input.
                if (!Uid.isValid(personId)) {
                    throw new ApiUidSyntaxException("personId", personId);
                }
                final Uid<Person> personUid = new Uid<Person>(personId);
                @Nullable final Person person = personDao.getPerson(personUid);

                if (person == null) {
                    throw new ApiNotFoundException();
                }
                LOG.debug("getPerson: personId={}, person={}", personId, person);

                // Build response.
                final PersonBinder binder = PersonConverter.fromDomain(person); // Create the binder.
                binder.validate();                                              // And validate it.
                response.setResponse(Response.ok(binder).build());

                // The response is already set within this method body.
                return Futures.successful(null);
            }
        });
    }

    @Override
    public void createPerson(
            @Nullable final PersonBinder personBinder,
            @Nonnull @Suspend(ApiConstants.SUSPEND_TIMEOUT) final AsynchronousResponse response) {
        assert response != null;

        processor.process(LOG, response, new ResourceHandler("createPerson(2)") {

            @Nonnull
            @Override
            public Future<?> process() throws Exception {

                // Check input.
                if (personBinder == null) {
                    throw new ApiParameterMissingException("person");
                }
                assert personBinder != null;
                if (personBinder.getId() != null) {
                    throw new ApiInvalidParameterCombinationException("id");
                }
                assert personBinder.getId() == null;

                // Create a new person ID on-the-fly.
                @Nonnull final Person person = PersonConverter.toDomain(personBinder);

                // Issue a log statement and store a human readable name for this new entity in logId().
                LOG.debug("createPerson: personId={}", logId(person.getId(), person.getName()));
                personDao.storePerson(person);

                // Create the response and validate it.
                @Nonnull final PersonBinder binder = PersonConverter.fromDomain(person);    // Create binder
                binder.validate();                                                          // And validate.

                // Build the response and return it.
                response.setResponse(Response.ok(binder).build());

                // The response is already set within this method body.
                return Futures.successful(null);
            }
        });
    }

    @Override
    public void removePerson(
            @Nonnull @PathParam(PARAM_PERSON_ID) final String personId,
            @Nonnull @Suspend(ApiConstants.SUSPEND_TIMEOUT) final AsynchronousResponse response) {
        assert personId != null;
        assert response != null;

        processor.process(LOG, response, new ResourceHandler("removePerson(2)") {

            @Nonnull
            @Override
            public Future<?> process() throws Exception {

                // Check input.
                if (!Uid.isValid(personId)) {
                    throw new ApiUidSyntaxException("personId", personId);
                }
                final Uid<Person> personUid = new Uid<Person>(personId);

                // Output a debug log statement which uses logId() to display a human-readable name if it exists.
                LOG.debug("removePerson: personId={}", logId(personId));


                @Nullable final Person person = personDao.getPerson(personUid);
                personDao.removePerson(personUid);

                LOG.debug("removePerson: personId={}, person={}", personId, person);
                if (person == null) {
                    throw new ApiNotFoundException();
                }

                // Build a "204 (NO CONTENT)" response. No binders required.
                response.setResponse(Response.noContent().build());

                // The response is already set within this method body.
                return Futures.successful(null);
            }
        });
    }
}