package im.dig.trial.messenger.services.model

import java.security.SecureRandom

import cats._
import cats.implicits._
import org.apache.commons.codec.binary.Hex


@SerialVersionUID(1L)
final case class Nickname(value: String)

object Nickname {
  implicit val readNickname: Read[Nickname] = { string =>
    if (string.length < 3)
      throw new IllegalArgumentException("Nickname should contain at least 3 characters")
    if (string.exists(!_.isLetterOrDigit))
      throw new IllegalArgumentException("Nickname should be alphanumeric")
    Nickname(string)
  }
  implicit val showNickname: Show[Nickname] = _.value
}


@SerialVersionUID(1L)
final case class SHA256(bytes: Array[Byte])

object SHA256 {
  private val random = new SecureRandom()
  def generate(): SHA256 = {
    val bytes = Array.fill[Byte](32)(0)
    random.nextBytes(bytes)
    SHA256(bytes)
  }
  implicit val showSHA256: Show[SHA256] =
    sha256 => Hex.encodeHexString(sha256.bytes)
  implicit val readSHA256: Read[SHA256] = string => {
    if (string.length =!= 64)
      throw new IllegalArgumentException(s"String '$string' is not a SHA256 hash")
    SHA256(Hex.decodeHex(string))
  }
}


@SerialVersionUID(1L)
final case class User (
  userId: HashId,
  nickname: Nickname
)

@SerialVersionUID(1L)
final case class Session (
  sessionId: HashId,
  userId: HashId
)



final case class ServiceUnavailable(serviceName: String)
  extends RuntimeException(s"Service '$serviceName' is unavailable.")
