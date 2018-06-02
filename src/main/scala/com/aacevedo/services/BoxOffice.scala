package com.aacevedo.services

import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import com.aacevedo.domain.{Event, Events, Ticket, Tickets}
import com.aacevedo.server.dto.{BoxOfficeMessages, TicketSellerMessages}
import com.aacevedo.server.dto.BoxOfficeMessages._
import com.aacevedo.server.dto.TicketSellerMessages.{Add, Buy, CancelEvent}

import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps

class BoxOffice(implicit ec: ExecutionContext, patience: Timeout) extends Actor {

  def createTicketSeller(name: String): ActorRef = context.actorOf(Props(new TicketSeller(name)), name)

  override def receive: Receive = {
    case BoxOfficeMessages.CreateEvent(event, amountOfTickets) =>
      def create(): Unit = {
        val eventTickets = createTicketSeller(event)
        val newTickets = (1 to amountOfTickets) map { ticketId =>
          Ticket(ticketId)
        }
        eventTickets ! Add(newTickets)
        sender ! EventCreated(Event(event, amountOfTickets))
      }

      context.child(event).fold(create())(_ => sender ! EventExists)
    case BoxOfficeMessages.GetTickets(event, amountOfTickets) =>
      context.child(event).fold(notFound(sender)(event))(_.forward(Buy(amountOfTickets)))
    case BoxOfficeMessages.GetEvents =>
      val originalSender = sender
      pipe(Future.sequence(getEvents) map toEvents) to originalSender
    case BoxOfficeMessages.GetEvent(event) =>
      context.child(event).fold(notFound(sender)(event))(ticketSeller => pipe(getEvent(ticketSeller)) to sender)
    case BoxOfficeMessages.CancelEvent(event) =>
      context.child(event).fold(notFound(sender)(event))(_.forward(CancelEvent))
  }

  private def notFound(sender: ActorRef)(event: String): Unit = { sender ! Tickets(event) }
  private def toEvents: Iterable[Option[Event]] => Events = { es => Events(es.flatten.toSeq) }
  private def getEvents: Iterable[Future[Option[Event]]] = context.children.map(getEvent)
  private def getEvent(ticketSeller: ActorRef): Future[Option[Event]] = (ticketSeller ? TicketSellerMessages.GetEvent).mapTo[Option[Event]]
}
