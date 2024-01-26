package toyoidc.http.responses

import zio.http.Path
import zio.json.{DeriveJsonEncoder, JsonEncoder}


case class NotFound(path: Path) extends ErrorDetail {
  override def errorCode: String = "NotFound"
}

object NotFound {
  private case class NotFoundJson(path: String, errorCode: String)

  implicit val encoder: JsonEncoder[NotFound] = DeriveJsonEncoder.gen[NotFoundJson].contramap(nf =>
    NotFoundJson(nf.path.toString(), nf.errorCode))
}

