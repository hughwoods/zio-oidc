package toyoidc.http.routes

import toyoidc.http.responses.NotFound
import zio.http.{Method, Path, Request, Response, RoutePattern, Status, handler, trailing}
import zio.json.EncoderOps

object Default {
  val route = Method.GET / trailing ->
    handler { (path: Path, req: Request) =>
      Response.json(NotFound(path).toJson).copy(status = Status.NotFound)
    }
}
