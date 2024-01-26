package toyoidc.http.responses

import zio.json.JsonEncoder

trait ErrorDetail {
  def errorCode: String
}
object ErrorDetail {
  implicit val detailEncoder: JsonEncoder[ErrorDetail] = JsonEncoder[String].contramap(detail => detail.errorCode)
}
