package ru.jr2.edit.domain.entity

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import ru.jr2.edit.data.db.table.MojiTable
import ru.jr2.edit.domain.misc.JlptLevel
import ru.jr2.edit.domain.misc.MojiType
import ru.jr2.edit.domain.model.Moji

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

fun MojiEntity.updateWithModel(model: Moji) {
    moji = model.moji
    strokeCount = model.strokeCount
    interpretation = model.interpretation
    frequency = model.frequency
    grade = model.grade
    svg = model.svg
    jlptLevel = JlptLevel.fromStr(model.jlptLevel).code
    mojiType = MojiType.fromStr(model.mojiType).code
}