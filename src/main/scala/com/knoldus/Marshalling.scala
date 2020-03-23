package com.knoldus

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat
import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn


case class User(name: String, age: Int)

object User {
  implicit val userJsonFormat: RootJsonFormat[User] = jsonFormat2(User.apply)

}

object Marshalling {

  def main(args: Array[String]): Unit = {

    val host = "0.0.0.0"
    val port = 8000

    implicit val system: ActorSystem = ActorSystem("system")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    def routeOne =
        path("demo") {
          get {
            complete(User("abc", 21))
          }
        }


    val bind = Http().bindAndHandle(routeOne, host, port)

    println(s"Server online at http://localhost:8000/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bind
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }

}
