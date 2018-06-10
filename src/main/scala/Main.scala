import akka.actor.ActorSystem
import akka.{Done, NotUsed}
import akka.stream._
import akka.stream.scaladsl._

import scala.concurrent.{ExecutionContextExecutor, Future}

object Main extends App {
  implicit val system: ActorSystem = ActorSystem("QuickStart")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  val source: Source[Int, NotUsed] = Source(1 to 100)
  val done: Future[Done] = source.runForeach(i ⇒ println(i))(materializer)

  done.onComplete(_ ⇒ system.terminate())
}