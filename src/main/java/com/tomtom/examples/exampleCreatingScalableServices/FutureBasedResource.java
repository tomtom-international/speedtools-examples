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

package com.tomtom.examples.exampleCreatingScalableServices;

import com.tomtom.examples.ApiConstants;
import com.tomtom.examples.exampleCreatingScalableServices.dto.VersionDTO;
import org.jboss.resteasy.annotations.Suspend;
import org.jboss.resteasy.spi.AsynchronousResponse;

import javax.annotation.Nonnull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * This example class provides all REST API calls with an Akka Futures based implementation.
 * <p>
 * Requests might in rare exception cases be answered with a HTTP 500 Internal Server Error.
 */
@Path("/")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public interface FutureBasedResource extends SimpleThreadBasedResource {

    /**
     * This API extends the REST API services from
     * {@link SimpleThreadBasedResource} and adds one to it.
     */

    /**
     * Additional method: this method returns the current version of the application.
     * <p>
     * Return HTTP status 200.
     *
     * @param response Version, {@link VersionDTO}.
     */
    @GET
    @Path("example/2/version")
    void getVersion(@Nonnull @Suspend(ApiConstants.SUSPEND_TIMEOUT) AsynchronousResponse response);

    /**
     * Add implementation of returning a favicon.
     *
     * @param response Favicon.
     */
    @GET
    @Path("favicon.ico")
    void getFavicon(@Nonnull @Suspend(ApiConstants.SUSPEND_TIMEOUT) AsynchronousResponse response);

    /**
     * Add implementation of returning something for the root URL.
     *
     * @param response Root URL response.
     */
    @GET
    void getRoot(@Nonnull @Suspend(ApiConstants.SUSPEND_TIMEOUT) AsynchronousResponse response);
}
