package ru.jr2.edit.data.db

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import ru.jr2.edit.data.db.table.*
import java.nio.file.Paths
import java.sql.Connection

class AppDatabase(isInMemory: Boolean = false) {
    private val dbPath = Paths.get("").toAbsolutePath()
    private val dbName = "jr2.db"
    private val connectionString = if (isInMemory) {
        "file:test?mode=memory&cache=shared"
    } else {
        "$dbPath\\$dbName"
    }

    val db: Database =
        Database.connect("jdbc:sqlite:$connectionString", "org.sqlite.JDBC")

    init {
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_READ_UNCOMMITTED
        updateSchema(db)
    }

    companion object {
        fun updateSchema(db: Database) = transaction(db) {
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
}