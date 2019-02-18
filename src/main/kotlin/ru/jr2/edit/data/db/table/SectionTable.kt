package ru.jr2.edit.data.db.table

import org.jetbrains.exposed.dao.IntIdTable

object SectionTable : IntIdTable("section") {
    val name = varchar("name", 100)
}