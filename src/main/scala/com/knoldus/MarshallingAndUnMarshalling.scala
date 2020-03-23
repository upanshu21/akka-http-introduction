package com.knoldus

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.Done
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat
import scala.language.implicitConversions
import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.io.StdIn

object MarshallingAndUnMarshalling {

  val host = "0.0.0.0"
  val port = 6000

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  // needed for the future map/flatMap in the end and future in fetchItem and saveOrder
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  var orders : List[Item] = Nil

    // domain model
  final case class Item(name: String, id: Long)
  final case class Order(items: List[Item])

  // formats for unmarshalling and marshalling
  implicit val itemFormat: RootJsonFormat[Item] = jsonFormat2(Item)
  implicit val orderFormat: RootJsonFormat[Order] = jsonFormat1(Order)

  // (fake) async database query api
  /**
   * This method is used to fetch items from the order list
   * @param itemId is the id of the order
   * @return item with the matching id
   */
  def fetchItems(itemId : Long) : Future[Option[Item]] = Future {
    orders.find(item => item.id == itemId)

  }

  /**
   * This method is used to save the order and save it to the list
   * @param order is the item to be saved
   * @return successful message
   */
  def saveOrder(order : Order) : Future[Done] = {

    orders = order match {
      case Order(items) => items ::: orders
      case _  => orders
    }
    Future{Done}
  }.fallbackTo( Future{ Done })

  def main(args: Array[String]): Unit = {

    val route : Route = {

    concat(
      get{
        path("item" / LongNumber) {
          id => val mayBeItem : Future[Option[Item]] = fetchItems(id)

            onSuccess(mayBeItem) {
              case Some(item) => complete(item)
              case None       => complete(StatusCodes.NotFound)
            }
        }
      },
      post{
        path("create-order") {
          entity(as[Order]) {
            order => val saved : Future[Done] = saveOrder(order)
              onComplete(saved) { done => complete("order created")}
          }
        }
      }

    )
  }

  val binding = Http().bindAndHandle(route, host, port)
  println(s"Server online at http://localhost:6000/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
  binding
    .flatMap(_.unbind())  // trigger unbinding from the port
    .onComplete(_ => system.terminate())  // and shutdown when done
  }


}

// curl -H "Content-Type: application/json" -X POST -d '{"items":[{"name":"hhgtg","id":42}]}' http://localhost:6000/create-order




