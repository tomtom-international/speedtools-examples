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

package com.tomtom.examples.exampleUsingLbsServices;

import akka.dispatch.Futures;
import akka.dispatch.Mapper;
import com.tomtom.examples.ApiConstants;
import com.tomtom.speedtools.apivalidation.exceptions.ApiForbiddenException;
import com.tomtom.speedtools.apivalidation.exceptions.ApiNotFoundException;
import com.tomtom.speedtools.geometry.GeoPoint;
import com.tomtom.speedtools.rest.Reactor;
import com.tomtom.speedtools.rest.ResourceProcessor;
import com.tomtom.speedtools.services.lbs.geocode.GeoCodeEngine;
import com.tomtom.speedtools.services.lbs.geocode.GeoCodeEngineResponse;
import com.tomtom.speedtools.services.lbs.geocode.GeoCodeEngineResult;
import com.tomtom.speedtools.services.lbs.route.RouteEngine;
import com.tomtom.speedtools.services.lbs.route.RouteEngineResponse;
import com.tomtom.speedtools.tracer.Traceable;
import com.tomtom.speedtools.tracer.TracerFactory;
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

/**
 * This class implements a REST API resource using the SpeedTools framework and includes a simple "tracer" which adds a
 * trace entry to the tracer MongoDB database.
 */
@SuppressWarnings({"OverlyBroadThrowsClause", "ProhibitedExceptionDeclared"})
public class ExampleLbsResourceImpl implements ExampleLbsResource {
    private static final Logger LOG = LoggerFactory.getLogger(ExampleLbsResourceImpl.class);
    private static final Tracer TRACER = TracerFactory.getTracer(ExampleLbsResourceImpl.class, Tracer.class);

    @Nonnull
    private final Reactor reactor;
    @Nonnull
    private final ResourceProcessor processor;
    @Nonnull
    private final GeoCodeEngine geoCodeEngine;
    @Nonnull
    private final RouteEngine routeEngine;

    @Inject
    public ExampleLbsResourceImpl(
            @Nonnull final Reactor reactor,
            @Nonnull final GeoCodeEngine geoCodeEngine,
            @Nonnull final RouteEngine routeEngine,
            @Nonnull final ResourceProcessor processor) {
        assert reactor != null;
        assert processor != null;
        assert geoCodeEngine != null;
        assert routeEngine != null;

        // Store the injected values.
        this.reactor = reactor;
        this.processor = processor;
        this.geoCodeEngine = geoCodeEngine;
        this.routeEngine = routeEngine;
    }

    @Override
    public void getTraceMe(
            @Nonnull @Suspend(ApiConstants.SUSPEND_TIMEOUT) final AsynchronousResponse response) {
        assert response != null;

        processor.process("getTraceMe", LOG, response, () -> {

            LOG.info("getTraceMe: This is me...");
            LOG.info("getTraceMe:");
            LOG.info("getTraceMe: The trace event should show up in the MongoDB database 'trace'");
            LOG.info("getTraceMe: in collection 'traces'. If MongoDB isn't running you probably");
            LOG.info("getTraceMe: got a (non-fatal) exception earlier and the trace event will only");
            LOG.info("getTraceMe: be issued to the logger (like these messages).");
            LOG.info("getTraceMe:");
            LOG.info("getTraceMe: Issueing a trace event now...");

            // Log a tracer call.
            TRACER.eventThisIsMe("This is me.");

            // Build the response and return it.
            response.setResponse(Response.noContent().build());

            // The response is already set within this method body.
            return Futures.successful(null);
        });
    }

    @Override
    public void getGeoCode(
            @Nonnull @PathParam(PARAM_QUERY) final String query,
            @Nonnull @Suspend(ApiConstants.SUSPEND_TIMEOUT) final AsynchronousResponse response) {
        assert response != null;

        processor.process("getGeoCode", LOG, response, () -> {
            LOG.info("getGeoCode: query={}", query);

            // Execute geocode call. Exceptions will be handled by the 'processor'.
            final GeoCodeEngineResponse r = geoCodeEngine.query(query);

            // Build the response and return it.
            response.setResponse(Response.ok(r).build());

            // The response is already set within this method body.
            return Futures.successful(null);
        });
    }

    @Override
    public void getRoute(
            @Nonnull @PathParam(PARAM_FROM) final String from,
            @Nonnull @PathParam(PARAM_TO) final String to,
            @Nonnull @Suspend(ApiConstants.SUSPEND_TIMEOUT) final AsynchronousResponse response) {
        assert response != null;

        processor.process("getRoute", LOG, response, () -> {

            LOG.info("getRoute: from={}, to={}", from, to);

            // Geocoding "from" query.
            final GeoCodeEngineResponse listFrom = geoCodeEngine.query(from);
            if (listFrom.getCount() <= 0) {
                throw new ApiNotFoundException();
            }
            final GeoCodeEngineResult resFrom = listFrom.getGeoCodeEngineResultList().get(0);
            final GeoPoint ptFrom = new GeoPoint((double) resFrom.getLatitude(), (double) resFrom.getLongitude());

            // Geocoding "to" query.
            final GeoCodeEngineResponse listTo = geoCodeEngine.query(to);
            if (listTo.getCount() <= 0) {
                throw new ApiNotFoundException();
            }
            final GeoCodeEngineResult resTo = listTo.getGeoCodeEngineResultList().get(0);
            final GeoPoint ptTo = new GeoPoint((double) resTo.getLatitude(), (double) resTo.getLongitude());

            // Execute routing call. Exceptions will be handled by the 'processor'.
            final Future<RouteEngineResponse> route = routeEngine.route(ptFrom, ptTo);

            // Map the future once it is available.
            return route.map(new Mapper<RouteEngineResponse, Void>() {
                @Nullable
                @Override
                public Void checkedApply(@Nullable final RouteEngineResponse parameter) throws Throwable {

                    if (parameter == null) {
                        LOG.error("getRoute: No response received from LBS");

                        // Probably, the system is reaching its limits.
                        throw new ApiForbiddenException("No response from LBS");
                    }

                    // Check whether the request was accepted.

                    // Verification code sent, respond ok.
                    response.setResponse(Response.ok(parameter).build());
                    return null;
                }
            }, reactor.getExecutionContext());
        });
    }

    /**
     * This local class defines the trace interface. The implementation of this class is provided by the SpeedTools
     * framework, which stores the call as a trace entry in a MongoDB database, together with its parameters and a time
     * stamp.
     * <p>
     * The traces can be read back with the SpeedTools framework as well. For playback, the developer may even provided
     * a custom implementation, so events can be replayed in real time (or slower/faster) and actually call real
     * implementations of the Tracer interface.
     * <p>
     * This is especially useful for visualizing trace data (where the implementation of the calls create visual
     * representations of the events, for example).
     */
    public static interface Tracer extends Traceable {

        /**
         * The REST API "version" was called.
         *
         * @param someText Some text.
         */
        void eventThisIsMe(@Nonnull final String someText);
    }
}
