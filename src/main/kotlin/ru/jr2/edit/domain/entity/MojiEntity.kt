package ru.jr2.edit.domain.entity

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import ru.jr2.edit.data.db.table.MojiTable

class MojiEntity(id: EntityID<Int>) : IntEntity(id) {
    val value by MojiTable.value
    val strokeCount by MojiTable.strokeCount
    val onReading by MojiTable.onReading
    val kunReading by MojiTable.kunReading
    val basicInterpretation by MojiTable.basicInterpretation
    val jlptLevel by MojiTable.jlptLevel
    val mojiType by MojiTable.mojiType

    companion object : IntEntityClass<MojiEntity>(MojiTable)
}