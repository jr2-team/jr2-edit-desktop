package ru.jr2.edit.data.db.table

import org.jetbrains.exposed.dao.IntIdTable

object WordTable : IntIdTable("word") {
    val word = varchar("word", 100).default("")
    val furigana = varchar("furigana", 500).nullable()
    val interpretation = varchar("interpretation", 10000).nullable()
    val jlptLevel = integer("jlpt_level")
}