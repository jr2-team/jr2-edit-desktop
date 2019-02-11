package ru.jr2.edit.data.db.table

import org.jetbrains.exposed.dao.IntIdTable

object WordTable: IntIdTable("word") {
    val value = varchar("value", 100)
    val furigana = varchar("furigana", 500).nullable()
    val interpretation = varchar("basic_interpretation", 500).nullable()
    val jlptLevel = integer("jlpt_level")
}