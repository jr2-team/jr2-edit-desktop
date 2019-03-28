package ru.jr2.edit.data.db.table

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption.NO_ACTION

object KanjiTable : IntIdTable("kanji") {
    val kanji = varchar("kanji", 100)
    val strokeCount = integer("stroke_count")
    val interp = varchar("interpretation", 500).nullable()
    val frequency = integer("frequency")
    val grade = integer("grade").nullable()
    val jlptLevel = integer("jlpt_level").nullable()
    val svg = varchar("svg", 10000).nullable()
    val radical = reference("radical_id", RadicalTable, NO_ACTION, NO_ACTION).nullable()
}