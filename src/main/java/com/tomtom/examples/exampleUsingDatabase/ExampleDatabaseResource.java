/*
 * Copyright (C) 2012-2019, TomTom (http://tomtom.com).
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

package com.tomtom.examples.exampleUsingDatabase;

import com.tomtom.examples.exampleCreatingScalableServices.SimpleThreadBasedResource;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * This example class provides a simple call to demonstrate use of the MongoDB tracer SpeedTools framework.
 *
 * For documentation, see {@link com.tomtom.examples.exampleCreatingScalableServices.FutureBasedResource}.
 */
@Path("/example/3")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public interface ExampleDatabaseResource extends SimpleThreadBasedResource {

    // Re-use the existing API. The base URL is different though (new version).
}
