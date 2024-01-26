package toyoidc.http.responses

import zio.json.{DeriveJsonEncoder, JsonEncoder}


case class ValidationError(parameter: String, error: ErrorDetail)

object ValidationError {
  def missingParameter(parameter: String) = ValidationError(parameter, ParameterMissing(parameter))

  implicit val encoder: JsonEncoder[ValidationError] = DeriveJsonEncoder.gen[ValidationError]
}