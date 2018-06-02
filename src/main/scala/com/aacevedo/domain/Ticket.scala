package com.aacevedo.domain

case class Ticket(id: Int)
case class Tickets(event: String, entries: Seq[Ticket] = Seq.empty[Ticket])
