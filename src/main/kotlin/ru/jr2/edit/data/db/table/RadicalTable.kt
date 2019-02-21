package ru.jr2.edit.data.db.table

import org.jetbrains.exposed.dao.IntIdTable

object RadicalTable : IntIdTable("radical") {
    val radical = varchar("radical", 1)
    val order = integer("order")
}