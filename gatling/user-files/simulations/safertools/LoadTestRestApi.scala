package speedtools

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
