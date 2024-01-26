package toyoidc.http.responses

import zio.Chunk
import zio.json.{DeriveJsonEncoder, JsonEncoder}

case class BadRequest(errors: Chunk[ValidationError])

object BadRequest {
  implicit val encoder: JsonEncoder[BadRequest] = DeriveJsonEncoder.gen[BadRequest]
}
