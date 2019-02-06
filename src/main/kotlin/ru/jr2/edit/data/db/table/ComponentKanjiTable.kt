package ru.jr2.edit.data.db.table

import org.jetbrains.exposed.dao.IntIdTable

object ComponentKanjiTable : IntIdTable("component_kanji") {
    val order = integer("order")
    val moji = optReference("moji_id", MojiTable)
    val mojiComponentId = optReference("moji_component_id", MojiTable)
}