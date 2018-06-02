package com.aacevedo.services

import akka.actor.{Actor, PoisonPill}
import com.aacevedo.domain.{Event, Ticket, Tickets}
import com.aacevedo.server.dto.TicketSellerMessages._

class TicketSeller(event: String) extends Actor {
  var tickets = Seq.empty[Ticket]
  override def receive: Receive = {
    case Add(newTickets) => tickets = tickets ++ newTickets
    case Buy(numberOfTickets) =>
      val entries = tickets.take(numberOfTickets)
      if (entries.size >= numberOfTickets) {
        sender ! Tickets(event, entries)
        tickets = tickets.drop(numberOfTickets)
      } else {
        sender ! Tickets(event)
      }
    case GetEvent =>
      sender ! Some(Event(event, tickets.size))
    case CancelEvent =>
      sender ! Some(Event(event, tickets.size))
      self ! PoisonPill
  }
}
