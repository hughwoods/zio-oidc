import toyoidc.http.WebApp
import zio.http.{Request, Status}
import zio.test._

object AuthSpec extends ZIOSpecDefault {
  def spec =
    suite("Auth Spec")(
      test("missing parameters returns bad request") {
        for {
          response <- WebApp.app(Request.get("auth"))
        } yield assertTrue(response.status == Status.BadRequest)
      }
    )
}