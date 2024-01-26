import toyoidc.http.WebApp
import zio.http.Request
import zio.test._

object PingSpec extends ZIOSpecDefault {
  def spec =
    suite("HelloWorldSpec")(
      test("Returns JSON version") {
        for {
          response <- WebApp.app(Request.get("ping"))
          bodyText <- response.body.asString
        } yield assertTrue(bodyText == "{\"version\":\"0.1.0\"}")
      }
    )
}