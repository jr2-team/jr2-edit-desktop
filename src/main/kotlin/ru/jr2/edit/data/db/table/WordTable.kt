package ru.jr2.edit.data.db.table

import org.jetbrains.exposed.dao.IntIdTable

object WordTable: IntIdTable("Word") {
    val value = varchar("value", 100)
    val furigana = varchar("furigana", 500).nullable()
    val basicInterpretation = varchar("basicInterpretation", 500).nullable()
    val jlptLevel = integer("jlptLevel").nullable()
}