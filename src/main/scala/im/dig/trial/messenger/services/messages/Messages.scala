package im.dig.trial.messenger.services.messages

import im.dig.trial.messenger.services.model.{Nickname, SessionId, UserId}


sealed abstract class CrudServiceMessage

sealed abstract class UserAction extends CrudServiceMessage
@SerialVersionUID(1L)
final case class CreateUser(userId: UserId, nickname: Nickname) extends UserAction
@SerialVersionUID(1L)
final case class ReadUser(userId: UserId) extends UserAction
@SerialVersionUID(1L)
final case class UpdateUser(userId: UserId, nickname: Nickname) extends UserAction
@SerialVersionUID(1L)
final case class DeleteUser(userId: UserId) extends UserAction
@SerialVersionUID(1L)
final case class FindUserByNickname(nickname: Nickname) extends UserAction

sealed abstract class SessionAction extends CrudServiceMessage
@SerialVersionUID(1L)
final case class CreateSession(sessionId: SessionId, userId: UserId) extends SessionAction
@SerialVersionUID(1L)
final case class ReadSession(sessionId: SessionId) extends SessionAction
@SerialVersionUID(1L)
final case class DeleteSession(sessionId: SessionId) extends SessionAction



sealed abstract class AuthServiceMessage
@SerialVersionUID(1L)
final case class GetUserId(sessionId: SessionId) extends AuthServiceMessage
