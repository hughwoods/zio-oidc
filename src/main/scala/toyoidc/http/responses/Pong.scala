package toyoidc.http.responses

import toyoidc.domain.Version
import zio.json.{DeriveJsonEncoder, JsonEncoder}

case class Pong(version: Version)

object Pong {
  implicit val pongEncoder: JsonEncoder[Pong] = DeriveJsonEncoder.gen[Pong]
}