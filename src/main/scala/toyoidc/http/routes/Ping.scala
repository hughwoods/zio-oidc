package toyoidc.http.routes

import toyoidc.domain.Version
import toyoidc.http.responses.Pong
import zio.http.{Method, Response, handler}
import zio.json.EncoderOps

object Ping {
  val route = Method.GET / "ping" -> handler(Response.json(Pong(Version(0, 1, 0)).toJson))
}
