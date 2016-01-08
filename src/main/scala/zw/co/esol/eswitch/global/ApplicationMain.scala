package zw.co.esol.eswitch.global

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import akka.actor.ActorSelection.toScala
import zw.co.esol.eswitch.handler.RequestHandlerActor
import akka.actor.Props
import zw.co.esol.eswitch.handler.BuncherActor
import zw.co.esol.eswitch.handler.SetTarget
import zw.co.esol.eswitch.handler.Queue
import zw.co.esol.eswitch.handler.Flush
import zw.co.esol.eswitch.handler.RequestHandlerActor.RequestMessage
import zw.co.esol.eswitch.handler.DangerousActor
import org.joda.time.DateTime

object ApplicationMain extends App {
 
  val system = ActorSystem("eswitch", ConfigFactory.load())
    
  //system.logConfiguration()
   
    val requestHandlerActor1 = system.actorOf(RequestHandlerActor.props, "requestHandlerActor")
  
    val requestHandlerActor = system.actorSelection("akka.tcp://eswitch@127.0.0.1:2552/user/requestHandlerActor")
    
    requestHandlerActor ! RequestHandlerActor.Initialize
    
    
    val start = DateTime.now
    println("Start persist 50000 transactions at: " + start)
    
    for(i <- 1 to 50000) { 
      requestHandlerActor ! RequestHandlerActor.RequestMessage("6*bal")
      println(i) 
    }
    
    val end = DateTime.now
    println("End persist 50000 transactions at: " + end)
     
    println("Time taken : " + (end.getMillis() - start.getMillis()))

//  val buncher = system.actorOf(Props[BuncherActor], "buncherActor")
//  
//  buncher ! SetTarget(requestHandlerActor1)
//  buncher ! Queue(RequestMessage("6*topup*2"))
//  buncher ! Queue(RequestMessage("6*bal"))
//  buncher ! Queue(RequestMessage("4*mini"))
//  buncher ! Flush
//  buncher ! Queue(RequestMessage("6*pay*cimas*20"))

  system.awaitTermination()
  
}