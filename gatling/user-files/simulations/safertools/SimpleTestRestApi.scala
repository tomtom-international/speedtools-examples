package speedtools

class SimpleTestRestApi extends Simulation {

  val httpConf = httpConfig.
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

  setUp(
    scnWarmUp.users(1).ramp(1).protocolConfig(httpConf))

  assertThat(
    global.successfulRequests.percent.is(100))
}
