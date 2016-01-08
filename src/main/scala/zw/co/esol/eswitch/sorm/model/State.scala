package zw.co.esol.eswitch.sorm.model

import zw.co.esol.eswitch.util.RefGen
import java.sql.Date
import org.joda.time.DateTime

case class State(
   val uuid: String = RefGen.getReference("E") + new java.util.Date().getTime().toString().substring(8) + (Math.random() * 100).toString(),
   val narrative: String = "",
   val status: String = "DRAFT",
   val dateCreated: DateTime = DateTime.now()
  )