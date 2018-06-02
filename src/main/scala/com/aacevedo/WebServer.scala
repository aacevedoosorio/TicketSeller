package com.aacevedo

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.aacevedo.api.RestApi
import com.aacevedo.server.RequestTimeout
import com.aacevedo.services.BoxOffice
import com.typesafe.config.ConfigFactory

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object WebServer extends App with RestApi with RequestTimeout {
  val config = ConfigFactory.load()
  val host = config.getString("http.host")
  val port = config.getInt("http.port")

  implicit val system: ActorSystem = ActorSystem("TicketSeller")
  implicit val ec: ExecutionContext = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  override implicit val timeout: Timeout = requestTimeout(config)

  override val createBoxOffice = system.actorOf(Props(new BoxOffice))

  val bindingFuture: Future[ServerBinding] = Http().bindAndHandle(routes, host, port)
  bindingFuture onComplete {
    case Success(s) => println(s"Server started ${s.localAddress}")
    case Failure(err) => println(s"Error while starting server ${err.getMessage}")
  }
}
