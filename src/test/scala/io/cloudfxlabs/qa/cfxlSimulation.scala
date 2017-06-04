package io.cloudfxlabs.qa

import java.io.File
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import com.typesafe.config._

class cfxlSimulation extends Simulation {


	// The following commandline variables loads from mvn command
	val users = Integer.getInteger("users", 1)
	val rUsers = java.lang.Long.getLong("rUsers", 0L)
	val env =  System.getProperty("env")
	var baseUrl =""
	var subUrl = ""

	println(s"No of users :  $users")
	println(s"No of rampUsers :  $rUsers")
	println(s"environment :  $env")

	if (env == "UAT")
	{
		val parsedConfig = ConfigFactory.parseFile(new File("src/test/resources/uat.conf"))
		val conf = ConfigFactory.load(parsedConfig)

		// The following config variables loads from uat.conf file
		baseUrl = conf.getString("load.config.baseUrl")
		subUrl = conf.getString("load.config.subUrl")

	} else if (env == "QA")
	{
		val parsedConfig = ConfigFactory.parseFile(new File("src/test/resources/qa.conf"))
		val conf = ConfigFactory.load(parsedConfig)

		// The following config variables loads from uat.conf file
		baseUrl = conf.getString("load.config.baseUrl")
		subUrl = conf.getString("load.config.subUrl")
	}

	println(s"Application baseUrl :  $baseUrl")
	println(s"Application subUrl :  $subUrl")


	val httpProtocol = http
		.baseURL(baseUrl)
		.inferHtmlResources()
		.acceptHeader("image/webp,image/*,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate, sdch")
		.acceptLanguageHeader("en-US,en;q=0.8,te;q=0.6")
		.userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")


	val scn = scenario("cfxlSimulation")
		.exec(http("request_0")
			.get("/img/loading.gif")
			.resources(http("request_1")
			.get("/cloudfxlabs.io/img/favicon/favicon.ico")
			.check(status.is(403)))
			.check(status.is(403)))

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}