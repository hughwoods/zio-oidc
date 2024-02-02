package toyoidc.inmemory

import toyoidc.domain.clients._
import zio._

case class InMemoryClientRegistry(register: Ref[Map[ClientId, Client]]) extends ClientRegistry {
  override def add(client: Client): IO[ClientRegistryUpdateError, Unit] =
    updateOrConflict(client).flatMap(optionalError)

  override def find(clientId: ClientId): IO[ClientRegistryReadError, Client] =
    for {
      maybeClient <- register.get.map(_.get(clientId))
      result <- clientOrNotFound(maybeClient)
    } yield result

  private def clientOrNotFound(maybeClient: Option[Client]) =
    maybeClient match {
      case Some(value) => ZIO.succeed(value)
      case None => ZIO.fail(ClientRegistryError.ClientNotFound)
    }

  private def updateOrConflict(client: Client) = register.modify(
    registerSnapshot =>
      if (registerSnapshot.keySet.contains(client.clientId))
        (Some(ClientRegistryError.Conflict), registerSnapshot)
      else
        (None, registerSnapshot + (client.clientId -> client))
  )

  private def optionalError[E](in: Option[E]): IO[E, Unit] =
    in match {
      case Some(value) => ZIO.fail(value)
      case None => ZIO.succeed()
    }
}

object InMemoryClientRegistry {
  private val inMemoryRegistryLayer = ZLayer.fromFunction(InMemoryClientRegistry.apply _)
  private val mapLayer = ZLayer.fromZIO(Ref.make(Map.empty[ClientId, Client]))
  val live = mapLayer >>> inMemoryRegistryLayer
}