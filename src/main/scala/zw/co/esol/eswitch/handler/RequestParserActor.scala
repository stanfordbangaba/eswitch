package zw.co.esol.eswitch.handler

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Props
import zw.co.esol.eswitch.handler.RequestParserActor.ParseRequest
import zw.co.esol.eswitch.handler.RequestHandlerActor.RequestMessage
import java.util.Date
import zw.co.esol.eswitch.sorm._
import zw.co.esol.eswitch.global.Dbase
import zw.co.esol.eswitch.global.Dbase.db
import zw.co.esol.eswitch.handler.RequestParserActor.ParseResponse
import zw.co.esol.eswitch.sorm.model.Transaction

class RequestParserActor extends Actor with ActorLogging {
  
  def receive = {
    case ParseRequest(text) =>
      log.info(s"Received ParseRequest > $text")
      sender ! parseRequest(text)
    case x: Any => 
      log.info(s"Unhandled request > $x") 
  }
  
  def parseRequest(text: String): ParseResponse = {
    val tokens = text.replace("#", "").split("\\*") 
    val messageType = tokens(1)
    val txn = new model.Transaction() 
    
    messageType match {
      case "bal" =>
        val txn2 = handleBalanceRequest(txn, tokens)
        ParseResponse(txn2.uuid, txn2.responseCode, txn2.narrative)
      case "topup" =>
        log.info("Topup msg..")
      case _ =>
        log.info("Hameno type iyi")
    }
    db.transaction {
      val state = db.save(new model.State())
      val ptxn = db.save(txn.copy(states = Seq(state)))
      log.info(s"Persisted txn: $ptxn")
    }
    ParseResponse(txn.uuid, txn.responseCode, txn.narrative)
  }
  
  def handleBalanceRequest(txn: model.Transaction, tokens: Array[String]): model.Transaction = {
    //6*bal or 6*bal*accountNumber
    log.info("Balance inquiry")
    //txn.messageType="bal"
    txn.copy(messageType="bal")
    tokens.length match {
      case 2 => 
        log.info("Setting primary account..")
       // txn.sourceMobile = "263773303509"
      case 3 =>
        log.info("Setting provided account..")
        //txn.sourceMobile = tokens(2)
      case _ =>
        //txn.responseCode = "05"
    }
    txn
  }
 
}

object RequestParserActor {
   val props = Props[RequestParserActor]
  case object Initialize
  case class ParseRequest(text: String)
  case class ParseResponse(messageId: String, responseCode: String, narrative: String)
}
