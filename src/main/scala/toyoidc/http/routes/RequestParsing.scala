package toyoidc.http.routes

import toyoidc.http.responses.ValidationError
import zio.http.QueryParams
import zio.prelude.Validation

object RequestParsing {
  def fromQuery(query: QueryParams)(name: String) ={
    val value = query.get(name)
    Validation.fromOptionWith(ValidationError.missingParameter(name))(value)
  }
}
