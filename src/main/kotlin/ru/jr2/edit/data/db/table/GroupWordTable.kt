package ru.jr2.edit.data.db.table

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption.CASCADE

object GroupWordTable : IntIdTable("group_word") {
    val defaultPosition = integer("default_position")
    val customPosition = integer("custom_position")
    val group = reference("group_id", GroupTable, CASCADE, CASCADE)
    val word = reference("word_id", WordTable, CASCADE, CASCADE)
}