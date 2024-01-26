package toyoidc.domain.auth

import zio.http.URL

case class AuthorisationRequest(
                               responseType: ResponseType,
                               clientId: String,
                               redirectUri: URL,
                               scope: Scope,
                               state: State
                               )