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

import org.jboss.resteasy.annotations.Suspend;
import org.jboss.resteasy.spi.AsynchronousResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.tomtom.examples.ApiConstants;
import com.tomtom.examples.exampleCreatingScalableServices.binders.PersonBinder;

/**
 * This class provides all REST API calls with a standard Thread based implementation.
 *
 * For documentation, see {@link FutureBasedResource}.
 */
@Path("/example/1")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public interface SimpleThreadBasedResource {

    public final String PARAM_PERSON_ID = "personId";

    /**
     * This method gets all IDs of existing Person records.
     *
     * HTTP 200: The person records were successfully retrieved.
     *
     * @param response Person, {@link com.tomtom.examples.exampleCreatingScalableServices.binders.PersonBinder}.
     */
    @GET
    @Path("person")
    void getPersons(
            @Nonnull @Suspend(ApiConstants.SUSPEND_TIMEOUT) AsynchronousResponse response);

    /**
     * This method gets an existing Person record.
     *
     * HTTP 200: The person record was successfully retrieved.
     *
     * HTTP 404: The person record was not found.
     *
     * @param personId Person to be retrieved from database.
     * @param response Person, {@link com.tomtom.examples.exampleCreatingScalableServices.binders.PersonBinder}.
     */
    @GET
    @Path("person/{" + PARAM_PERSON_ID + '}')
    void getPerson(
            @Nonnull @PathParam(PARAM_PERSON_ID) String personId,
            @Nonnull @Suspend(ApiConstants.SUSPEND_TIMEOUT) AsynchronousResponse response);

    /**
     * This method creates a new Person record. The ID should not be specified in the personBinder (considered an
     * error). The response contains full record, including the ID.
     *
     * HTTP 200: The person record was successfully created.
     *
     * @param personBinder Person to be created in database. Must not contain "id" field.
     * @param response     New record, {@link com.tomtom.examples.exampleCreatingScalableServices.binders.PersonBinder}.
     */
    @POST
    @Path("person")
    void createPerson(
            @Nullable PersonBinder personBinder,
            @Nonnull @Suspend(ApiConstants.SUSPEND_TIMEOUT) AsynchronousResponse response);

    /**
     * This method deletes an existing Person record.
     *
     * HTTP 204: The person record was successfully deleted.
     *
     * HTTP 400: If the ID was not the correct UUID format. You are allowed to use the abbreviated 1-1-1-1-1 form.
     *
     * HTTP 404: The person record was not found.
     *
     * @param personId Person to be deleted from database.
     * @param response ID of newly created record.
     */
    @DELETE
    @Path("person/{" + PARAM_PERSON_ID + '}')
    void removePerson(
            @Nonnull @PathParam(PARAM_PERSON_ID) String personId,
            @Nonnull @Suspend(ApiConstants.SUSPEND_TIMEOUT) AsynchronousResponse response);
}