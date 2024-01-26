package toyoidc.http

import toyoidc.http.routes.Ping
import zio.{ZIO, ZIOAppDefault}
import zio.http._

object WebApp extends ZIOAppDefault {

  val app: HttpApp[Any] =
    Routes(
      Ping.route
    ).toHttpApp

  val startUp = ZIO.debug("Starting server ...")

  val server =  startUp *> Server.serve(app)

  override val run = server.provide(Server.default)
}
