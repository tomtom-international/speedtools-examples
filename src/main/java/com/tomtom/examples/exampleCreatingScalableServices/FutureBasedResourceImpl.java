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

package com.tomtom.examples.exampleCreatingScalableServices;

import akka.dispatch.Futures;
import com.tomtom.examples.exampleCreatingScalableServices.converters.IdConverter;
import com.tomtom.examples.exampleCreatingScalableServices.converters.PersonConverter;
import com.tomtom.examples.exampleCreatingScalableServices.domain.Person;
import com.tomtom.examples.exampleCreatingScalableServices.dto.IdDTO;
import com.tomtom.examples.exampleCreatingScalableServices.dto.IdsDTO;
import com.tomtom.examples.exampleCreatingScalableServices.dto.PersonDTO;
import com.tomtom.examples.exampleCreatingScalableServices.dto.VersionDTO;
import com.tomtom.speedtools.apivalidation.exceptions.*;
import com.tomtom.speedtools.domain.Uid;
import com.tomtom.speedtools.maven.MavenProperties;
import com.tomtom.speedtools.rest.ResourceProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.ws.rs.PathParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * This class implements the REST API resources using the SpeedTools framework.
 */
public class FutureBasedResourceImpl implements FutureBasedResource {
    private static final Logger LOG = LoggerFactory.getLogger(FutureBasedResourceImpl.class);

    @Nonnull
    private final ResourceProcessor processor;
    @Nonnull
    private final MavenProperties mavenProperties;
    @Nonnull
    private final SimulatedBusyDatabase db;

    /**
     * Define your REST API calls in a class and have the constructor of the class get the Akka reactor, the processor
     * and any required properties injected.
     *
     * @param processor       The processor allows calls to be executed on virtual Akka threads (in futures) in a simple
     *                        way.
     * @param mavenProperties These are additional properties, in this case to satisfy the "version" request.
     * @param db              The 'database interface' to used. This is added as an example of how to inject your own
     *                        singletons.
     */
    @Inject
    public FutureBasedResourceImpl(
            @Nonnull final ResourceProcessor processor,
            @Nonnull final MavenProperties mavenProperties,
            @Nonnull final SimulatedBusyDatabase db) {
        assert processor != null;
        assert mavenProperties != null;
        assert db != null;

        // Store the injected values.
        this.processor = processor;
        this.mavenProperties = mavenProperties;
        this.db = db;
    }

    /**
     * This is the actual REST API call. Note that the response is an asynchronous response. The response type is
     * entirely type safe and validated in the corresponding "binder" object in the package "domain" (the use of names
     * like binder and domain is convention only).
     */
    @Override
    public void getVersion(
            @Suspended @Nonnull final AsyncResponse response) {
        assert response != null;

        /**
         * The actual processing of the request should be scheduled on a virtual Akka thread, so we
         * transfer the processing of the request to the "processor" as soon as we get into this method.
         *
         * The processor has a method called "process()" which
         * should return a future successful. Setting the response is always done explicitly in this method.
         * The framework will automatically choose the right object mapper (JSON, XML, ...).
         */
        processor.process("getVersion", LOG, response, () -> {

            // No input validation required. Just return version number.
            final String pomVersion = mavenProperties.getPomVersion();
            LOG.info("getVersion: POM version={}", pomVersion);

            // Create the response binder and validate it (returned objects must also be validated!).
            // Validation errors are automatically caught as exceptions and returned by the framework.
            final VersionDTO binder = new VersionDTO(pomVersion); // Create a binder.
            binder.validate();                                          // You must validate it before using it.

            // Build the response and return it.
            response.resume(Response.ok(binder).build());

            // The response is already set within this method body.
            return Futures.successful(null);
        });
    }

    @Override
    public void getFavicon(
            @Suspended @Nonnull final AsyncResponse response) {
        assert response != null;

        processor.process("getFavicon", LOG, response, () -> {
            response.resume(Response.noContent().build());
            return Futures.successful(null);
        });
    }

    @Override
    public void getRoot(
            @Suspended @Nonnull final AsyncResponse response) {
        assert response != null;
        getVersion(response);
    }

    @Override
    public void getPersons(
            @Suspended @Nonnull final AsyncResponse response) {
        assert response != null;

        processor.process("getPersons", LOG, response, () -> {
            @Nonnull final Set<IdDTO> idDTOs = new HashSet<>();
            final Enumeration<Uid<Person>> e = db.keys();
            while (e.hasMoreElements()) {
                @Nonnull final IdDTO idDTO = IdConverter.fromDomain(e.nextElement());
                idDTOs.add(idDTO);
            }

            // Build response.
            @Nonnull final IdsDTO binder = new IdsDTO(idDTOs); // Create the binder.
            binder.validate();                                          // And validate it before returning!
            response.resume(Response.ok(binder).build());

            // The response is already set within this method body.
            return Futures.successful(null);
        });
    }

    @Override
    public void getPerson(
            @Nonnull @PathParam(PARAM_PERSON_ID) final String personId,
            @Suspended @Nonnull final AsyncResponse response) {
        assert personId != null;
        assert response != null;

        processor.process("getPerson", LOG, response, () -> {

            // Check input.
            if (!Uid.isValid(personId)) {
                throw new ApiUidSyntaxException("personId", personId);
            }
            final Uid<Person> personUid = new Uid<>(personId);
            @Nullable final Person person = db.get(personUid);

            LOG.debug("getPerson: personId={}, person={}", personId, person);
            if (person == null) {
                throw new ApiNotFoundException();
            }

            // Build response.
            final PersonDTO binder = PersonConverter.fromDomain(person); // Create the binder.
            binder.validate();                                              // And validate it.
            response.resume(Response.ok(binder).build());

            // The response is already set within this method body.
            return Futures.successful(null);
        });
    }

    @Override
    public void createPerson(
            @Nullable final PersonDTO personDTO,
            @Suspended @Nonnull final AsyncResponse response) {
        assert response != null;

        processor.process("createPerson", LOG, response, () -> {

            // Check input.
            if (personDTO == null) {
                throw new ApiParameterMissingException("person");
            }
            assert personDTO != null;
            if (personDTO.getId() != null) {
                throw new ApiInvalidParameterCombinationException("id");
            }
            assert personDTO.getId() == null;

            // Create a new person ID on-the-fly.
            @Nonnull final Person person = PersonConverter.toDomain(personDTO);

            LOG.debug("createPerson: personId={}", person.getId());
            if (db.putIfAbsent(person) != null) {
                throw new ApiDuplicateIdException("id", person.getId().toString());
            }

            // Create the response and validate it.
            @Nonnull final PersonDTO binder = PersonConverter.fromDomain(person);    // Create binder
            binder.validate();                                                          // And validate.

            // Build the response and return it.
            response.resume(Response.ok(binder).build());

            // The response is already set within this method body.
            return Futures.successful(null);
        });
    }

    @Override
    public void removePerson(
            @Nonnull @PathParam(PARAM_PERSON_ID) final String personId,
            @Suspended @Nonnull final AsyncResponse response) {
        assert personId != null;
        assert response != null;

        processor.process("removePerson", LOG, response, () -> {

            // Check input.
            if (!Uid.isValid(personId)) {
                throw new ApiUidSyntaxException("personId", personId);
            }
            final Uid<Person> personUid = new Uid<>(personId);
            @Nullable final Person person = db.remove(personUid);

            LOG.debug("removePerson: personId={}, person={}", personId, person);
            if (person == null) {
                throw new ApiNotFoundException();
            }

            // Build a "204 (NO CONTENT)" response. No binders required.
            response.resume(Response.noContent().build());

            // The response is already set within this method body.
            return Futures.successful(null);
        });
    }
}
