package com.aacevedo.api

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.aacevedo.api.RestApi._
import com.aacevedo.domain.{Event, Events, Ticket, Tickets}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val eventFormat: RootJsonFormat[Event] = jsonFormat2(Event)
  implicit val eventsFormat: RootJsonFormat[Events] = jsonFormat1(Events)
  implicit val errorFormat: RootJsonFormat[TickerError] = jsonFormat1(TickerError)
  implicit val ticketFormat: RootJsonFormat[Ticket] = jsonFormat1(Ticket)
  implicit val ticketsFormat: RootJsonFormat[Tickets] = jsonFormat2(Tickets)
  implicit val eventDescriptionFormat: RootJsonFormat[EventDescription] = jsonFormat1(EventDescription)
  implicit val ticketRequestFormat: RootJsonFormat[TicketRequest] = jsonFormat1(TicketRequest)

}
