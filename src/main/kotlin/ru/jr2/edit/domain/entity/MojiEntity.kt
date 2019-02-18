package ru.jr2.edit.domain.entity

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import ru.jr2.edit.data.db.table.MojiTable

class MojiEntity(id: EntityID<Int>) : IntEntity(id) {
    var moji by MojiTable.moji
    var strokeCount by MojiTable.strokeCount
    var interpretation by MojiTable.interpretation
    var frequency by MojiTable.frequency
    var grade by MojiTable.grade
    var svg by MojiTable.svg
    var jlptLevel by MojiTable.jlptLevel
    var mojiType by MojiTable.mojiType

    companion object : IntEntityClass<MojiEntity>(MojiTable)
}