package ru.jr2.edit.data.db.table

import org.jetbrains.exposed.dao.IntIdTable

object MojiTable : IntIdTable("moji") {
    val value = varchar("value", 100)
    val strokeCount = integer("stroke_count")
    val onReading = varchar("on_reading", 500).nullable()
    val kunReading = varchar("kun_reading", 500).nullable()
    val interpretation = varchar("basic_interpretation", 500).nullable()
    val jlptLevel = integer("jlpt_level")
    val mojiType = integer("moji_type")
}