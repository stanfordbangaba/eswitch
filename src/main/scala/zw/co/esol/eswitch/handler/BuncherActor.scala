package zw.co.esol.eswitch.handler

import akka.actor.FSM
import akka.actor.ActorRef
import scala.collection._
import scala.concurrent.duration._
import akka.actor.ActorLogging

//received events
case class SetTarget(ref: ActorRef)
case class Queue(obj: Any)
case object Flush

//sent events
case class Batch(obj: immutable.Seq[Any])

// states
sealed trait State
case object Idle extends State
case object Active extends State
sealed trait Data
case object Uninitialized extends Data
case class Todo(target: ActorRef, queue: immutable.Seq[Any]) extends Data

class BuncherActor extends FSM[State, Data] with ActorLogging {
  
  startWith(Idle, Uninitialized) 
  
  when(Idle) {
    case Event(SetTarget(ref), Uninitialized) =>
      log.info("Received SetTarget.. stay in Idle")
      stay using Todo(ref, Vector.empty)
  }
   
  when(Active, stateTimeout = 1 second) {
    case Event(Flush | StateTimeout, t: Todo) =>
      log.info("Received Flush | StateTimeout.. goto Idle")
      goto(Idle) using t.copy(queue = Vector.empty)
  }
  
  initialize()
  
  whenUnhandled {
    // common code for both states
    case Event(Queue(obj), t @ Todo(_, v)) =>
      log.info(s"Unhandled event.. queue $obj .. goto Active")
      goto(Active) using t.copy(queue = v :+ obj)
    case Event(e, s) =>
      log.warning("received unhandled request {} in state {}/{}", e, stateName, s)
      stay
  }
  
  onTransition {
    case Active -> Idle =>
      stateData match {
        case Todo(ref, queue) => 
          log.info("onTransition.. flush batch")
          ref ! Batch(queue)
        case Uninitialized =>
          log.info("onTransition.. uninitialized")
      }
}

  
  
}