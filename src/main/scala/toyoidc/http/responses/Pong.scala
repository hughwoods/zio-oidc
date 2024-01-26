package toyoidc.http.responses

import toyoidc.domain.Version
import zio.json.DeriveJsonEncoder

case class Pong(version: Version) extends Product

object Pong {
  implicit val pongEncoder = DeriveJsonEncoder.gen[Pong]
}