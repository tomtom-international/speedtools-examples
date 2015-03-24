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

package com.tomtom.examples.exampleCreatingScalableServices;

import com.tomtom.examples.ApiConstants;
import com.tomtom.examples.exampleCreatingScalableServices.binders.IdBinder;
import com.tomtom.examples.exampleCreatingScalableServices.binders.IdBinders;
import com.tomtom.examples.exampleCreatingScalableServices.binders.PersonBinder;
import com.tomtom.examples.exampleCreatingScalableServices.converters.IdConverter;
import com.tomtom.examples.exampleCreatingScalableServices.converters.PersonConverter;
import com.tomtom.examples.exampleCreatingScalableServices.domain.Person;
import com.tomtom.speedtools.apivalidation.exceptions.*;
import com.tomtom.speedtools.domain.Uid;
import org.jboss.resteasy.annotations.Suspend;
import org.jboss.resteasy.spi.AsynchronousResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * This example class implements the REST API resources using the SpeedTools framework.
 *
 * Note that this implementation does not have the nice flat scaling characteristics as the Akka Futures-based
 * implementation in {@link com.tomtom.examples.exampleCreatingScalableServices.FutureBasedResourceImpl}.
 */
public class SimpleThreadBasedResourceImpl implements SimpleThreadBasedResource {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleThreadBasedResourceImpl.class);

    @Nonnull
    private final SimulatedBusyDatabase db;

    /**
     * Define your REST API calls in a class and have the constructor of the class get the Akka reactor, the processor
     * and any required properties injected.
     *
     * @param db Database.
     */
    @Inject
    public SimpleThreadBasedResourceImpl(
            @Nonnull final SimulatedBusyDatabase db) {

        // Store the injected values.
        this.db = db;
    }

    @Override
    public void getPersons(
            @Nonnull @Suspend(ApiConstants.SUSPEND_TIMEOUT) final AsynchronousResponse response) {
        assert response != null;

        @Nonnull final Set<IdBinder> idBinders = new HashSet<IdBinder>();
        final Enumeration<Uid<Person>> e = db.keys();
        while (e.hasMoreElements()) {
            @Nonnull final IdBinder idBinder = IdConverter.fromDomain(e.nextElement());
            idBinders.add(idBinder);
        }

        // Build response.
        LOG.debug("getPersons: idBinders={}", idBinders);
        @Nonnull final IdBinders binder = new IdBinders(idBinders);
        binder.validate();
        response.setResponse(Response.ok(binder).build());
    }

    @Override
    public void getPerson(
            @Nonnull @PathParam(PARAM_PERSON_ID) final String personId,
            @Nonnull @Suspend(ApiConstants.SUSPEND_TIMEOUT) final AsynchronousResponse response) {
        assert personId != null;
        assert response != null;

        // Check input.
        if (!Uid.isValid(personId)) {
            throw new ApiUidSyntaxException("personId", personId);
        }
        final Uid<Person> personUid = new Uid<Person>(personId);
        @Nullable final Person person = db.get(personUid);

        LOG.debug("getPerson: personId={}, person={}", personId, person);
        if (person == null) {
            throw new ApiNotFoundException();
        }

        // Build response.
        final PersonBinder binder = PersonConverter.fromDomain(person);
        binder.validate();
        response.setResponse(Response.ok(binder).build());
    }

    @Override
    public void createPerson(
            @Nullable final PersonBinder personBinder,
            @Nonnull @Suspend(ApiConstants.SUSPEND_TIMEOUT) final AsynchronousResponse response) {
        assert response != null;

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

        LOG.debug("createPerson: personId={}", person.getId());
        if (db.putIfAbsent(person) != null) {
            throw new ApiDuplicateIdException("id", person.getId().toString());
        }

        // Create the response and validate it.
        @Nonnull final PersonBinder binder = PersonConverter.fromDomain(person);
        binder.validate();

        // Build the response and return it.
        response.setResponse(Response.ok(binder).build());
    }

    @Override
    public void removePerson(
            @Nonnull @PathParam(PARAM_PERSON_ID) final String personId,
            @Nonnull @Suspend(ApiConstants.SUSPEND_TIMEOUT) final AsynchronousResponse response) {
        assert personId != null;
        assert response != null;

        // Check input.
        if (!Uid.isValid(personId)) {
            throw new ApiUidSyntaxException("personId", personId);
        }
        final Uid<Person> personUid = new Uid<Person>(personId);
        @Nullable final Person person = db.remove(personUid);

        LOG.debug("removePerson: personId={}, person={}", personId, person);
        if (person == null) {
            throw new ApiNotFoundException();
        }

        // Build response.
        response.setResponse(Response.noContent().build());
    }
}
