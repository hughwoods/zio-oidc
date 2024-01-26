package toyoidc.domain

import zio.json.JsonEncoder
import zio.json.internal.Write

case class Version(major: Int, minor: Int, patch: Int) extends Product {
  override def toString: String = s"${major}.${minor}.${patch}"
}

object Version {
  implicit val versionEncoder: JsonEncoder[Version] =
    (a: Version, indent: Option[Int], out: Write) =>
      out.write(
        implicitly[JsonEncoder[String]]
        .encodeJson(a.toString, indent)
        .toString
      )
}