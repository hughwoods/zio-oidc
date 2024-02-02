package toyoidc.domain.clients

import zio.Chunk
import zio.http.URL

sealed trait Client {
  def clientId: ClientId
  def name: String
}

object Client {
  case class SinglePageClient(name: String, clientId: ClientId, redirectUris: Chunk[URL]) extends Client
}