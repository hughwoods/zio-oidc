package toyoidc.http

import toyoidc.http.routes.Ping
import zio.ZIOAppDefault
import zio.http._

object WebApp extends ZIOAppDefault {

  val app: HttpApp[Any] =
    Routes(
      Ping.route
    ).toHttpApp

  val server = Server.serve(app)

  override val run = server.provide(Server.default)
}