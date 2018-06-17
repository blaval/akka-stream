package example

import akka.stream.scaladsl._
import akka.{Done, NotUsed}

import scala.concurrent.Future

object Main extends App {

  import shared.Implicits._

  val source: Source[Int, NotUsed] = Source(1 to 100)
  val done: Future[Done] = source.runForeach(i ⇒ println(i))

  done.onComplete(_ ⇒ system.terminate())
}