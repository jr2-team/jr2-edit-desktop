package ru.jr2.edit.data.db.table

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption.CASCADE

object SentenceWordTable : IntIdTable("sentence_word") {
    val sentence = reference("sentence_id", SentenceTable, CASCADE, CASCADE)
    val word = reference("word_id", WordTable, CASCADE, CASCADE)
}