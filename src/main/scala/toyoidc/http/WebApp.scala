package toyoidc.http

import toyoidc.http.routes._
import toyoidc.inmemory.InMemoryClientRegistry
import toyoidc.toy.ToyResourceOwner
import zio.http._
import zio.{ZIO, ZIOAppDefault}

object WebApp extends ZIOAppDefault {
  val app =
    Routes(
      Ping.route,
      Auth(new ToyResourceOwner()).route,
      RegistrationController.liveRoute,
      Default.route
    ).toHttpApp

  val startUp = ZIO.debug("Starting server ...")

  val server = startUp *> Server.serve(
    app @@ Middleware.debug
  )

  override val run = server.provide(
    Server.defaultWithPort(80),
    RegistrationController.live,
    ClientRegistrationService.live,
    InMemoryClientRegistry.live
  )
}
