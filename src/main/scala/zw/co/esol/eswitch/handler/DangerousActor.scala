package zw.co.esol.eswitch.handler

import akka.actor.ActorLogging
import akka.actor.Actor
import akka.pattern.CircuitBreaker
import akka.pattern.pipe
import scala.concurrent.duration._
import scala.concurrent.Future
import akka.actor.Props

class DangerousActor extends Actor with ActorLogging {
 
  import context.dispatcher
  
  val breaker = new CircuitBreaker(
    context.system.scheduler,
    maxFailures = 5,
    callTimeout = 10.seconds,
    resetTimeout = 1.minute).onOpen(notifyMeOnOpen)
  
  def notifyMeOnOpen: Unit = {
    log.info("====== My CircuitBreaker is now open, and will not close for one minute")
  }
  
  def dangerousCall: String = {
    "This really isn’t that dangerous of a call after all"
  }
  def receive = {
    case "breaker async" =>
      log.info("Calling circuit breaker async..")
      breaker.withCircuitBreaker(Future(dangerousCall)) pipeTo sender()
    case "breaker sync" =>
      log.info("Calling circuit breaker sync..")
      sender() ! breaker.withSyncCircuitBreaker(dangerousCall)
  }
  
}

object DangerousActor {
  val props = Props[DangerousActor]
}