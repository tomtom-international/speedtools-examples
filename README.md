# Read Me for TomTom SpeedTools Library Examples

Copyright (C) 2012-2015, TomTom International BV. All rights reserved.
----

The module speedtools-examples explicitly does not depend on the parent POM of the project.
This is to simulate using SpeedTools in a stand-alone environment, where you would normally
depend only on SpeedTools JARs from Nexus, not on the project POM itself.

To run the REST API example, type:

    cd speedtools-examples
    mvn jetty:run
    
To test the web services, enter the following URL in your web browser:    
    
    http://localhost:8080/example/2/version
    
Or use a tool like cURL:    
    
    curl -X GET http://localhost:8080/example/2/version

This should produce a JSON object, similar to:
 
    {"version":"3.0.0"}
     
Have fun!

**Rijn Buve**

*TomTom International BV*

# License

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

# Example Source Code

This section clarifies the organation of the example source code.
The example source code is organized as follows:

    com.tomtom.examples
    |
    +-- common
    |   StartUpCheck.java - Example of a safe start-up check module for an application.
    |
    +-- restapi
        |
        |   This package contains common API constants (like timeouts) and a Guice
        |   binder for services.
        |
        +-- service
            |
            |   This package contains the API interface definitions and their
            |   implementations.
            |
            |   *Resource.java      - Interface API specification for a REST API.
            |   *ResourceImpl.java  - REST API implementation.
            |
            +-- binders
            |   This package contains data binders for data transfer in APIs.
            |
            +-- converters
            |   This package contains converters to convert data between API and domain
            |   object formats.
            |
            +-- domain
                This package contains all domain object definitions.

    resources
    |
    |   This directory contains the property files used in the examples as well as the
    |   log4j.xml initialization file.
    
    webapp
    |
    |   This is the Resteasy web application configuration directory.
    ----

# SpeedTools Examples

The SpeedTools library contains two examples to show how to use the library. One shows how you can use
SpeedTools to create a REST API server. The other examples shows how to use SpeedTools to create a
simple command-line application (can be run without an application container).

The module `examples` does <<not>> reuse the parent POM properties. This is to make
it slightly easier to reuse the examples in a completely separate context, where SpeedTools is imported
only as <<JAR>> files from a Nexus repository and you don't have access to the parent POM of the
SpeedTools projects.

So, please refer to the `examples` module as a starting point for <<copy-paste>> development
practices.

## REST API Example

SpeedTools allows you to create efficient, safe and highly scalably REST web services.

The example `com.tomtom.examples.restapi` shows a small framework of how to
use the SpeedTools components for REST services.

Build the library and samples by executing:

    cd <SAFERTOOL-HOME>
    mvn clean install

Then, start the REST API server in a Jetty server:

    cd <SAFERTOOL-HOME>/examples/samples
    mvn jetty:run

You can experiment with the REST API calls. The calls offered are:

- `GET /web/1/version` - This returns a POM version of the application. It shows how you can inject a
  Maven modified property into an application to show a reliable version/release number.

- `POST /web/1/person/[id]` - This creates a new person in the "database" with ID <<[id]>>. The JSON values
should look like this: `{"id":"00000001-0001-0001-0001-000000000001","name":"My Name","age":40}`. You can
use the abbreviated UUID form of `1-1-1-1-1` as well (but it must be that form or the full form - others will
be rejected). This example also shows how input validation on different types of properties is done: UUID
(format check), String (min/max length check) and Integer (min/max value).

- `GET /web/1/person/[id]` - This gets the person with ID <<[id]>> from the database or returns <<404>> if not
found.

- `DELETE /web/1/person/[id]` - This removes the person with ID <<[id]>> from the database or returns <<404>>
 if not found.


## Testing the REST API using CURL

You can test the REST API calls using curl on a command-line. We'll show a couple of examples.

Creating a new record is done by POSTing an entity (without an ID) to the base URL of resource. It returns
the full record, including the ID.

    curl -X POST http://localhost:8080/web/1/person -H "Content-Type: application/json" -d '{"name":"John","age":20}'

Returns:

    {"id":"58b8d571-6a3c-4ca1-b56c-2c6f28a301cb","name":"John","age":20}

Getting a list of all person IDs:

    curl -X GET http://localhost:8080/web/1/person/

Returns:

    {"ids":[{"id":"00000001-0001-0001-0001-000000000001"},{"id":"00000001-0001-0001-0001-000000000002"}]}

Getting a specific person:

    curl -X GET http://localhost:8080/web/1/person/1-1-1-1-1
    curl -X GET http://localhost:8080/web/1/person/1-1-1-1-1 -H "Accept: application/json"

Returns:

    {"id":"00000001-0001-0001-0001-000000000001","name":"Sample User","age":40}

Same, in XML:

    curl -X GET http://localhost:8080/web/1/person/1-1-1-1-1 -H "Accept: application/xml"

Returns:

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?><person><age>40</age>
    <id>00000001-0001-0001-0001-000000000001</id><name>Sample User</name></person>

Deleting a person:

    curl -X DELETE http://localhost:8080/web/1/person/1-1-1-1-1

Returns nothing (`204 NO_CONTENT`). Or:

    curl -X DELETE http://localhost:8080/web/1/person/9-9-9-9-9

Returns:

    {"message":"ApiNotFoundException","reference":"REF-A7E4B007-4D09-4A4C-AC7F-3BA56E68DCFB-X",
    "time":"2014-02-27T12:03:35Z"}

## Using the SpeedTools MongoDB Trace Framework

The example includes a web service "version 3" with the implementation of `GET /web/3/traceme`
which uses the exceptionally powerful SpeedTools MongoDB tracer framework.

This framework allows to you to define "trace events" as type safe Java interfaces, which are safely
logged asynchronously to a MongDB instance. The events can be replayed with your own event implementation
(for example, for visualization or debugging purposes).

To execute an example call, type:

    curl -X GET http://localhost:8080/web/3/traceme

And then examine the MongoDB database:

    $ mongo
    
    > show dbs
    local	0.078125GB
    trace	0.265625GB
    
    > use trace
    switched to db trace
    
    > db.traces.find()
    { "_id" : ObjectId("531d8cd6da060f57d1ef40cb"), "time" : ISODate("2014-03-10T09:58:46.135Z"),
    "clazz" : "com.tomtom.examples.restapi.service.TracingResourceImpl", "tracer" :
    "com.tomtom.examples.restapi.service.TracingResourceImpl$Tracer", "method" :
    "apiVersion", "args" : [  "1.5.1-SNAPSHOT" ], "serial" : NumberLong(0),
    "@class" : "com.tomtom.speedtools.tracer.mongo.MongoDBTrace" }

The returned record shows which trace interface was called, as well as its arguments.

The implementation of an event playback system looks something like this. In the event playback class,
add a `MongoDBTraceStream` to be injected:

    @Inject
    public MyEventPlaybackClass(
        @Nonnull final MongoDBTraceStream events,
        ...) {
        ...
        events.addTraceHandler(MyVersionTraceHandler);
        events.addTraceHandler(new MyTraceHandler());
    }

In a Guice binding module, add the singleton for the implementation of the version trace:

    binder.bind(MyVersionTraceHandler.class).in(Singleton.class);

And add a specific handler for the "version" trace:

    public final class MyVersionTraceHandler implements TracingResource.Tracer {
    
        @Override
        public void version(@Nonnull final String version) {
            // Use the version for something
        }
    }

And you may use a generic trace handler as well, for events you cannot recognize
with type safe interface implementations:

    public class MyTraceHandler implements GenericTraceHandler {
    
        @Override
        public void handle(
            @Nonnull final DateTime time,
            @Nonnull final String clazz,
            @Nonnull final String tracer,
            @Nonnull final String method,
            @Nonnull final Object[] args) {
                // Do something with the parameters.
        }
    }

To start the event playback, add the following statement in your main application:

    events.playbackToEnd();

Et voila, the framework will call your trace implementation for every trace event found
in the database.

The `MongoDBTraceStream` interface offers other functions as well for playback.
Please refer to the API documentation for more info.

## Using the TomTom LBS Services wiht the SpeedTools Trace Framework

Web service "version 3" also include simple examples of using the TomTom LBS services for
geocoding and routing. You can access them by typing:

    curl -X GET http://localhost:8080/web/3/geocode/<query>

And:

    curl -X GET http://localhost:8080/web/3/route/<from>/<to>/

Currently, the returned route information contains only travel time information; not the
actual route, but this may changed fairly easily.


## Load Testing REST API Services

The source tree of the `examples` module contains a directly `gatling` which is the <<Gatling>>
load test tool (see <<gatling-tool.org>>).

We have provided 2 implementation of the REST APIs to run a load test against:

- a default Resteasy Thread-based implementation in `ThreadBasedResource/Impl` and

- an Akka Futures-based implementation called `FutureBasedResource/Impl`.


The thread-based version is available under `http://.../web/1/...` and the future-based version
under `http://.../web/2/...`.

We have also defined a Gatling scenario that runs a load test on one implementation and then on
the other and generates a combined report. Run it by typing executing the following commands:

    cd <SPEEDTOOLS-HOME>
    mvn clean install
    
    cd examples
    mvn jetty:run

Then, in another terminal execute:

    cd <SPEEDTOOLS-HOME>
    cd examples/gatling
    bin/gatling.sh

When the Gatling menu appears, choose option `0` for the first scenario.

# Using Git and `.gitignore`

It's good practice to set up a personal global `.gitignore` file on your machine which filters a number of files
on your file systems that you do not wish to submit to the Git repository. You can set up your own global
`~/.gitignore` file by executing:
`git config --global core.excludesfile ~/.gitignore`

In general, add the following file types to `~/.gitignore` (each entry should be on a separate line):
`*.com *.class *.dll *.exe *.o *.so *.log *.sql *.sqlite *.tlog *.epoch *.swp *.hprof *.hprof.index *.releaseBackup *~`

If you're using a Mac, filter:
`.DS_Store* Thumbs.db`

If you're using IntelliJ IDEA, filter:
`*.iml *.iws .idea/`

If you're using Eclips, filter:
`.classpath .project .settings .cache`

If you're using NetBeans, filter: 
`nb-configuration.xml *.orig`

The local `.gitignore` file in the Git repository itself to reflect those file only that are produced by executing
regular compile, build or release commands, such as:
`target/ out/`

# Bug Reports and New Feature Requests

If you encounter any problems with this library, don't hesitate to use the `Issues` session to file your issues.
Normally, one of our developers should be able to comment on them and fix. 

