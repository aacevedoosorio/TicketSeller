package com.aacevedo.api

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import com.aacevedo.domain.{Event, Events, Tickets}
import com.aacevedo.server.dto.BoxOfficeMessages._

import scala.concurrent.{ExecutionContext, Future}

trait BoxOfficeApi {
  implicit val ec: ExecutionContext
  def createBoxOffice(): ActorRef
  implicit def timeout: Timeout

  lazy val boxOffice: ActorRef = createBoxOffice()
  def createEvent(event: String, tickets: Int): Future[Any] = boxOffice.ask(CreateEvent(event, tickets))
  def getEvents: Future[Events] = boxOffice.ask(GetEvents).mapTo[Events]
  def getEvent(event: String): Future[Option[Event]] = boxOffice.ask(GetEvent(event)).mapTo[Option[Event]]
  def cancelEvent(event: String): Future[Option[Event]] = boxOffice.ask(CancelEvent(event)).mapTo[Option[Event]]
  def requestTickets(event: String, tickets: Int): Future[Tickets] = boxOffice.ask(GetTickets(event, tickets)).mapTo[Tickets]
}
