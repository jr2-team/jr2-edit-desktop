package ru.jr2.edit.data.db.table

import org.jetbrains.exposed.dao.IntIdTable

object SentenceTable : IntIdTable("sentence") {
    val sentence = SentenceTable.varchar("sentence", 1000)
    val furigana = SentenceTable.varchar("furigana", 1500).nullable()
    val interpretation = SentenceTable.varchar("interpretation", 1500).nullable()
}