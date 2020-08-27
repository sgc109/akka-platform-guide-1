package shopping.analytics

import akka.actor.typed.ActorSystem
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.AbstractBehavior
import akka.actor.typed.scaladsl.ActorContext
import akka.actor.typed.scaladsl.Behaviors
import akka.management.cluster.bootstrap.ClusterBootstrap
import akka.management.scaladsl.AkkaManagement

object Main {

  def main(args: Array[String]): Unit = {
    ActorSystem[Nothing](Main(), "ShoppingAnalytics")
  }

  def apply(): Behavior[Nothing] = {
    Behaviors.setup[Nothing](context => new Main(context))
  }
}

class Main(context: ActorContext[Nothing]) extends AbstractBehavior[Nothing](context) {
  val system = context.system

  startAkkaManagement()

  ShoppingCartEventConsumer.init(system)

  // can be overridden in tests
  protected def startAkkaManagement(): Unit = {
    AkkaManagement(system).start()
    ClusterBootstrap(system).start()
  }

  override def onMessage(msg: Nothing): Behavior[Nothing] =
    this
}
