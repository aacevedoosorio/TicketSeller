package com.aacevedo.domain

case class Event(name: String, tickets: Int)
case class Events(events: Seq[Event])
