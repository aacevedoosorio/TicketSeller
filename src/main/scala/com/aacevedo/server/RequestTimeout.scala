package com.aacevedo.server

import akka.util.Timeout
import com.typesafe.config.Config
import scala.language.implicitConversions

trait RequestTimeout {
  import scala.concurrent.duration._
  def requestTimeout(config: Config): Timeout = {
    val t = config.getString("request-timeout")
    val d = Duration(t)
    FiniteDuration(d.length, d.unit)
  }
}
