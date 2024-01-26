package toyoidc.http.responses

import zio.json.{DeriveJsonEncoder, JsonEncoder}

case class ParameterMissing(location: String) extends ErrorDetail {
  override def errorCode: String = "ParameterMissing"
}

object ParameterMissing {
  private case class ParameterMissingJson(location: String, errorCode: String)

  implicit val encoder: JsonEncoder[ParameterMissing] = DeriveJsonEncoder.gen[ParameterMissingJson].contramap(pm =>
    ParameterMissingJson(pm.location, pm.errorCode))
}

