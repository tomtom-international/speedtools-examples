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

  val scnWarmUp = scenario("Warm-Up").
    exec(http("first-thread").get("/example/1/person").headers(headers)).
    exec(http("first-future").get("/example/2/person").headers(headers))

  val scnThread = scenario("Thread").exec(http("thread").get("/example/1/person").headers(headers))

  val scnFuture = scenario("Futures").exec(http("future").get("/example/2/person").headers(headers))

  val nrUsers = 1000
  val rampSecs = 30

  setUp(scnWarmUp.inject(atOnceUsers(1)),
    scnThread.inject(nothingFor(10 seconds), rampUsers(500) over (30 seconds)),
    scnFuture.inject(nothingFor(90 seconds), rampUsers(500) over (30 seconds))).
    protocols(httpConf).
    assertions(global.successfulRequests.percent.is(100))
}
