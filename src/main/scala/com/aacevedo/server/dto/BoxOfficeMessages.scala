package com.aacevedo.server.dto

import com.aacevedo.domain.Event

object BoxOfficeMessages {
  sealed trait EventCommand
  case class CreateEvent(name: String, tickets: Int) extends EventCommand
  case class GetEvent(name: String) extends EventCommand
  case object GetEvents extends EventCommand
  case class GetTickets(event: String, tickets: Int) extends EventCommand
  case class CancelEvent(name: String) extends EventCommand

  sealed trait EventResponse
  case class EventCreated(event: Event) extends EventResponse
  case object EventExists extends EventResponse
}
