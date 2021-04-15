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

import com.tomtom.examples.exampleCreatingScalableServices.dto.PersonDTO;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

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
     * @param response Person, {@link PersonDTO}.
     */
    @GET
    @Path("person")
    void getPersons(
            @Suspended @Nonnull AsyncResponse response);

    /**
     * This method gets an existing Person record.
     *
     * HTTP 200: The person record was successfully retrieved.
     *
     * HTTP 404: The person record was not found.
     *
     * @param personId Person to be retrieved from database.
     * @param response Person, {@link PersonDTO}.
     */
    @GET
    @Path("person/{" + PARAM_PERSON_ID + '}')
    void getPerson(
            @Nonnull @PathParam(PARAM_PERSON_ID) String personId,
            @Suspended @Nonnull AsyncResponse response);

    /**
     * This method creates a new Person record. The ID should not be specified in the personBinder (considered an
     * error). The response contains full record, including the ID.
     *
     * HTTP 200: The person record was successfully created.
     *
     * @param personDTO Person to be created in database. Must not contain "id" field.
     * @param response  New record, {@link PersonDTO}.
     */
    @POST
    @Path("person")
    void createPerson(
            @Nullable PersonDTO personDTO,
            @Suspended @Nonnull AsyncResponse response);

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
            @Suspended @Nonnull AsyncResponse response);
}
