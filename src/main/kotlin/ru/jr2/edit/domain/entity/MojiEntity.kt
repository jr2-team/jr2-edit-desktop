package ru.jr2.edit.domain.entity

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import ru.jr2.edit.data.db.table.MojiTable

class MojiEntity(id: EntityID<Int>) : IntEntity(id) {
    var value by MojiTable.value
    var strokeCount by MojiTable.strokeCount
    var onReading by MojiTable.onReading
    var kunReading by MojiTable.kunReading
    var basicInterpretation by MojiTable.basicInterpretation
    var jlptLevel by MojiTable.jlptLevel
    var mojiType by MojiTable.mojiType

    companion object : IntEntityClass<MojiEntity>(MojiTable)
}