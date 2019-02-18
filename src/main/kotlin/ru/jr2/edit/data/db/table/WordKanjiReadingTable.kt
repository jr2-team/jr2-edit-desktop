package ru.jr2.edit.data.db.table

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption.CASCADE

object WordKanjiReadingTable : IntIdTable("word_kanji_reading") {
    val word = reference("word_id", WordTable, CASCADE, CASCADE)
    val kanjiReading = reference("kanji_reading_id", KanjiReadingTable, CASCADE, CASCADE)
}