package zw.co.esol.eswitch.sorm.model

import zw.co.esol.eswitch.util.RefGen
import java.util.Date
import org.joda.time.DateTime

case class Transaction(
   val uuid: String = RefGen.getReference("E") + new Date().getTime().toString().substring(8) + (Math.random() * 100).toString(),
   val messageType: String = "bal",
   val sourceMobile: String = "263773303509",
   val targetMobile: String = "",
   val amount: Long = 0,
   val responseCode: String = "00",
   val narrative: String = "",
   val status: String = "DRAFT",
   val states: Seq[State] = Nil,
   val dateCreated: DateTime = DateTime.now()
  )

