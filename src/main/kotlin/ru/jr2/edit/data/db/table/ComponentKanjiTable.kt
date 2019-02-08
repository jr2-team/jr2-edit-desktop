package ru.jr2.edit.data.db.table

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption.CASCADE

object ComponentKanjiTable : IntIdTable("component_kanji") {
    val order = integer("order")
    val moji = reference("moji_id", MojiTable, CASCADE, CASCADE)
    val mojiComponentId = reference("moji_component_id", MojiTable, CASCADE, CASCADE)
}