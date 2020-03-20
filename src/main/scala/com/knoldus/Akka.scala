package com.knoldus

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

object Server extends App {

  val host = "0.0.0.0"
  val port = 9000

  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher

  def route =
    path("hello")
    {  get {    complete("Hello, World!")  }}

  Http().bindAndHandle(route, host, port)
}
