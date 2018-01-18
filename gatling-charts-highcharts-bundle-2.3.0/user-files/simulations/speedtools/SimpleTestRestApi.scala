package speedtools

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class SimpleTestRestApi extends Simulation {

  val httpConf = http.
    baseURL("http://localhost:8080").
    acceptCharsetHeader("utf-8").
    acceptHeader("application/json").
    acceptLanguageHeader("en-US").
    disableFollowRedirect

  val headers = Map(
    "Accept" -> "application/json")

  val scnWarmUp = scenario("Warm-Up").
    exec(http("person").get("/example/1/person").headers(headers)).
    exec(http("person").get("/example/2/person").headers(headers))

  setUp(scnWarmUp.inject(atOnceUsers(1))).
    protocols(httpConf).
    assertions(global.successfulRequests.percent.is(100))
}
