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

package com.tomtom.examples.exampleUsingLbsServices;

import javax.annotation.Nonnull;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

/**
 * This example class provides a simple call to demonstrate use of the MongoDB tracer SpeedTools framework.
 *
 * For documentation, see {@link com.tomtom.examples.exampleCreatingScalableServices.FutureBasedResource}.
 */
@Path("/lbs")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public interface ExampleLbsResource {

    public final String PARAM_QUERY = "query";
    public final String PARAM_FROM = "from";
    public final String PARAM_TO = "to";

    /**
     * This call executes a very simple web services call and issues a trace event to a MongoDB server using the
     * SpeedTools framework. The trace events ends up in the 'trace' database, collection 'traces'. To view the traces
     * in a MongoDB shell, execute:
     * <pre>
     *     mongo
     *     use trace
     *     db.traces.find()
     * </pre>
     *
     * @param response Response.
     */
    @GET
    @Path("traceme")
    void getTraceMe(
            @Suspended @Nonnull AsyncResponse response);

    /**
     * This call executes a geocoding call to TomTom LBS.
     *
     * @param query    Query.
     * @param response See {@link com.tomtom.speedtools.services.lbs.geocode.GeoCodeEngineResponse}.
     */
    @GET
    @Path("geocode/{" + PARAM_QUERY + '}')
    void getGeoCode(
            @Nonnull @PathParam(PARAM_QUERY) String query,
            @Suspended @Nonnull AsyncResponse response);

    /**
     * This call executes a geocoding call to TomTom LBS.
     *
     * @param from     From.
     * @param to       To.
     * @param response See {@link com.tomtom.speedtools.services.lbs.route.RouteEngineResponse}.
     */
    @GET
    @Path("route/{" + PARAM_FROM + "}/{" + PARAM_TO + '}')
    void getRoute(
            @Nonnull @PathParam(PARAM_FROM) String from,
            @Nonnull @PathParam(PARAM_TO) String to,
            @Suspended @Nonnull AsyncResponse response);
}
