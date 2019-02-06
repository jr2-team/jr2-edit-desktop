package ru.jr2.edit.data.db.table

import org.jetbrains.exposed.dao.IntIdTable

object WordTable: IntIdTable("word") {
    val value = varchar("value", 100)
    val furigana = varchar("furigana", 500)
    val basicInterpretation = varchar("basicInterpretation", 500)
    val jlptLevel = integer("jlptLevel").nullable()
}