package ru.jr2.edit.data.db.table

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption.CASCADE

object  WordInterpretationTable : IntIdTable("word_interpretation") {
    val interpretation = varchar("interpretation", 2000).default("")
    val pos = varchar("pos", 100)
    val language = varchar("language", 10)
    val word = reference("word_id", WordTable, CASCADE, CASCADE)
}