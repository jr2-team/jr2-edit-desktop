package ru.jr2.edit.data.db.table

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption.CASCADE

object KanjiReadingTable : IntIdTable("kanji_reading") {
    val reading = varchar("reading", 20).default("")
    val readingType = integer("reading_type")
    val priority = integer("priority")
    val isAnachronism = bool("is_anachronism").default(false)
    val kanji = reference("kanji_id", KanjiTable, CASCADE, CASCADE)
}