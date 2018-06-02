package com.aacevedo.server.dto

import com.aacevedo.domain.Ticket

object TicketSellerMessages {
  sealed trait TicketSellerCommand
  case class Add(tickets: Seq[Ticket]) extends TicketSellerCommand
  case class Buy(tickets: Int) extends TicketSellerCommand
  case object GetEvent extends TicketSellerCommand
  case object CancelEvent extends TicketSellerCommand
}
