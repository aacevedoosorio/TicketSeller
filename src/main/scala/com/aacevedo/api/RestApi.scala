package com.aacevedo.api
import com.aacevedo.server.dto.BoxOfficeMessages
import akka.http.scaladsl.server.{Directives, Route}
import akka.http.scaladsl.model.StatusCodes._
import com.aacevedo.api.RestApi.{EventDescription, TickerError, TicketRequest}

object RestApi {
  case class TickerError(message: String)
  case class EventDescription(tickets: Int) {
    requireTicketsAmount(tickets)
  }
  case class TicketRequest(tickets: Int) {
    requireTicketsAmount(tickets)
  }
  private def requireTicketsAmount(tickets: Int): Unit = require(tickets > 0)
}

trait RestApi extends BoxOfficeApi with Directives with JsonSupport {
  def routes: Route = eventRoute ~ ticketRoute ~ eventsRoute
  private def eventRoute: Route =
    pathPrefix("events" / Segment) { event =>
      pathEndOrSingleSlash {
        post {
          //POST /events/:event
          entity(as[EventDescription]) { eventDescription =>
            onSuccess(createEvent(event, eventDescription.tickets)) {
              case BoxOfficeMessages.EventCreated(ev) => complete(Created, ev)
              case BoxOfficeMessages.EventExists => complete(BadRequest, TickerError(s"The event $event already exists"))

            }
          }
        }  ~
        get {
          // GET /events/:event
          onSuccess(getEvent(event)) {
            case Some(ev) => complete(ev)
            case None => complete(NotFound)
          }
        } ~
        delete {
          // DELETE /events/:event
          onSuccess(cancelEvent(event)) {
            case Some(ev) => complete(ev)
            case None => complete(NotFound)
          }
        }
      }
    }

  private def eventsRoute: Route =
    pathPrefix("events") {
      get {
        // GET /events/
        onSuccess(getEvents) { events =>
          complete(events)
        }
      }
    }

  private def ticketRoute: Route =
    pathPrefix("events" / Segment / "tickets") { event =>
      post {
        pathEndOrSingleSlash {
          // POST /events/:event/tickets
          entity(as[TicketRequest]) { ticketRequest =>
            onSuccess(requestTickets(event, ticketRequest.tickets)) { tickets =>
              tickets.entries.toList match {
                case Nil =>
                  complete(NotFound)
                case _ => complete (Created, tickets)
              }
            }
          }
        }
      }
    }
}
