package ru.jr2.edit.data.db.table

import org.jetbrains.exposed.dao.IntIdTable

object KanjiTable : IntIdTable("kanji") {
    val kanji = varchar("kanji", 100)
    val strokeCount = integer("stroke_count")
    val interpretation = varchar("interpretation", 500).nullable()
    val frequency = integer("frequency")
    val grade = integer("grade").nullable()
    val svg = varchar("svg", 10000).nullable()
    val jlptLevel = integer("jlpt_level").nullable()
    // TODO: Добавить поле "радикал"
}