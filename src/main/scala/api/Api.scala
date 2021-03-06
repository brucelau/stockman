package com.mlh.stockman.api

import com.mlh.stockman.core.{ CoreActors, Core }
import akka.actor.{ ActorRefFactory, Props }
import spray.routing.RouteConcatenation
import akka.routing._

trait Api extends RouteConcatenation {
  this: CoreActors with Core =>

  private implicit val _ = system.dispatcher

  val routes =
      new PortfoliosRoute(portfolio).route ~
      new StatsRoute(system).route ~
      new Site() { override def actorRefFactory: ActorRefFactory = system }.route

  val rootService = system.actorOf(Props(new RoutedHttpService(routes)).withRouter(new RoundRobinRouter(5)))

}
