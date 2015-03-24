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

package speedtools

import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._
import assertions._

class LoadTestRestApi extends Simulation {

  val httpConf = httpConfig.
    baseURL("http://localhost:8080").
    acceptCharsetHeader("utf-8").
    acceptHeader("application/json").
    acceptLanguageHeader("en-US").
    disableFollowRedirect

  val headers = Map(
    "Accept" -> "application/json")

  val scnWarmUp = scenario("Warm-Up").
    exec(http("first-thread").get("/example/1/person").headers(headers)).
    exec(http("first-future").get("/example/2/person").headers(headers))

  val scnThread = scenario("Thread").exec(http("thread").get("/example/1/person").headers(headers))

  val scnFuture = scenario("Futures").exec(http("future").get("/example/2/person").headers(headers))

  val nrUsers = 1000
  val rampSecs = 30

  setUp(
    scnWarmUp.users(1).ramp(1).protocolConfig(httpConf),
    scnThread.users(500).ramp(30).protocolConfig(httpConf).delay(10),
    scnFuture.users(500).ramp(30).protocolConfig(httpConf).delay(90))

  assertThat(
    global.successfulRequests.percent.is(100))
}
