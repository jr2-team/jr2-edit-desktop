package ru.jr2.edit.data.db.table

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption.CASCADE

object KanjiComponentTable : IntIdTable("kanji_component") {
    val order = integer("order")
    val kanji = reference("moji_id", KanjiTable, CASCADE, CASCADE)
    val kanjiComponentId = reference("moji_component_id", KanjiTable, CASCADE, CASCADE)
}