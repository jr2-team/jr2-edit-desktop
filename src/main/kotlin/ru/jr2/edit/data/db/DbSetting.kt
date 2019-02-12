import org.jetbrains.exposed.sql.Database
import java.nio.file.Paths

object DbSetting {
    val db by lazy {
        Database.connect("jdbc:sqlite:$dbPath\\$dbName", "org.sqlite.JDBC")
    }

    private val dbPath = "${Paths.get("").toAbsolutePath()}\\"
    private val dbName = "jr2.db"
}