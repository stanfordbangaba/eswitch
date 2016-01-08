package zw.co.esol.eswitch.handler

import akka.actor.Actor
import akka.actor.Props
import zw.co.esol.eswitch.handler.RequestHandlerActor.Initialize
import akka.actor.ActorLogging
import zw.co.esol.eswitch.handler.RequestHandlerActor.RequestMessage
import zw.co.esol.eswitch.handler.RequestParserActor.ParseRequest
import akka.actor.OneForOneStrategy
import akka.actor.SupervisorStrategy._
import akka.routing.ActorRefRoutee
import akka.routing.Router
import akka.routing.RoundRobinRoutingLogic
import akka.actor.Terminated
import zw.co.esol.eswitch.handler.RequestParserActor.ParseResponse


class RequestHandlerActor extends Actor with ActorLogging {
  
  override val supervisorStrategy = OneForOneStrategy() {
    case _: Exception => Restart
  }

  var count = 0
  
  var router = {
    val routees = Vector.fill(10) {
      val r = context.actorOf(RequestParserActor.props)
      context watch r
      ActorRefRoutee(r)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }
    
  def receive = {
    
    case text: String =>
      log.info(s"Handler: Received text > $text")
      router.route(ParseRequest(text), sender);
    case Initialize => 
      log.info("In RequestHandlerActor - starting init")
    case RequestMessage(text) =>
      log.info(s"Text - received message: ${text}")
      router.route(ParseRequest(text), sender);
    case pr: ParseResponse =>
      log.info(s"Reply: $pr")
    case Terminated(actor) =>
      router = router.removeRoutee(actor)
      val r = context.actorOf(RequestParserActor.props)
      context watch r
      router = router.addRoutee(r)
    case a: Any =>
      log.info("Unhandled message: {}", a)
  } 
 
}
 
object RequestHandlerActor {
  val props = Props[RequestHandlerActor]
  case object Initialize
  case class RequestMessage(text: String)
  case class ResponseMessage(text: String)
}