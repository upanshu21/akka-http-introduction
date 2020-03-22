package com.knoldus

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.concurrent.duration._
import scala.util.{Failure, Success}


object AkkaServer extends App {

  val host = "0.0.0.0"
  val port = 9000

  implicit val system: ActorSystem = ActorSystem("my-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  def route =
    path("hello")
    {  get {    complete("Hello, World!")  }}

  val binding = Http().bindAndHandle(route, host, port)  //to run our server we need to bind the route to a host and a port
  binding.onComplete{
    case Success(_) => println("sucess")
    case Failure(error) => println(s"Failed : ${error.getMessage}")
  }

  Await.result(binding, 3.seconds)



}
