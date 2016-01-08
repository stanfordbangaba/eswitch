

package zw.co.esol.eswitch.global
 
import sorm._
import zw.co.esol.eswitch.sorm._

object Dbase {
  
  private val entitySet = Set(
      Entity[model.Transaction](unique = Set() + Seq("uuid"), indexed = Set() + Seq("uuid", "sourceMobile", "status")),
      Entity[model.State](unique = Set() + Seq("uuid"), indexed = Set() + Seq("uuid", "status"))
    )

  val db = new Instance(
    entities = entitySet,
    url = "jdbc:mysql://localhost/eswitch?useUnicode=yes&characterEncoding=UTF-8",
    user = "root",
    password = "",
    initMode = InitMode.DoNothing,
    poolSize = 20
  )  
  
}