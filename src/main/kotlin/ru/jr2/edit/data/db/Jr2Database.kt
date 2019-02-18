import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import ru.jr2.edit.data.db.table.*
import java.nio.file.Paths
import java.sql.Connection

object Jr2Database {
    private val dbPath = "${Paths.get("").toAbsolutePath()}\\"
    private val dbName = "jr2.db"

    val db = Database.connect("jdbc:sqlite:$dbPath\\$dbName", "org.sqlite.JDBC")

    init {
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_READ_UNCOMMITTED
        createSchema(db)
    }

    private fun createSchema(db: Database) = transaction(db) {
        SchemaUtils.createMissingTablesAndColumns(
            WordTable,
            MojiTable,
            SentenceTable,
            SectionTable,
            WordInterpretationTable,
            KanjiReadingTable,
            ComponentKanjiTable,
            GroupTable,
            GroupWordTable,
            GroupKanjiTable,
            WordKanjiReadingTable,
            SentenceWordTable
        )
    }
}