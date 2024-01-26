package toyoidc.http

import toyoidc.http.routes._
import toyoidc.toy.ToyResourceOwner
import zio.{ZIO, ZIOAppDefault}
import zio.http._

object WebApp extends ZIOAppDefault {
  val app: HttpApp[Any] =
    Routes(
      Ping.route,
      Auth(new ToyResourceOwner()).route,
      Default.route
    ).toHttpApp

  val startUp = ZIO.debug("Starting server ...")

  val server =  startUp *> Server.serve(
    app @@ Middleware.debug
  )

  override val run = server.provide(Server.defaultWithPort(80))
}
