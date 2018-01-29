package speedtools

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class LoadTestRestApi extends Simulation {

  val httpConf = http.
    baseURL("http://localhost:8080").
    acceptCharsetHeader("utf-8").
    acceptHeader("application/json").
    acceptLanguageHeader("en-US").
    disableFollowRedirect

  val headers = Map(
    "Accept" -> "application/json")

  val scenarioWarmUp = scenario("Warm-Up").
    exec(http("first-thread").get("/example/1/person").headers(headers)).
    exec(http("first-future").get("/example/2/person").headers(headers))

  val scenarioNonAkkaImplementation = scenario("NonAkkaImplementation").
    exec(http("NonAkkaImplementation").get("/example/1/person").headers(headers))

  val scenarioAkkaImplementation = scenario("AkkaImplementation").
    exec(http("AkkaImplementation").get("/example/2/person").headers(headers))

  setUp(
    scenarioWarmUp.inject(atOnceUsers(1)),
    scenarioAkkaImplementation.inject(nothingFor(10 seconds), rampUsers(5000) over (30 seconds))).
    //scenarioAkkaImplementation.inject(nothingFor(180 seconds), rampUsers(5000) over (30 seconds))).
    protocols(httpConf).
    assertions(global.successfulRequests.percent.is(100))
}
