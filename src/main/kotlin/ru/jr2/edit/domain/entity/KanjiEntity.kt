package ru.jr2.edit.domain.entity

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import ru.jr2.edit.data.db.table.KanjiTable
import ru.jr2.edit.domain.misc.JlptLevel
import ru.jr2.edit.presentation.model.KanjiModel

class KanjiEntity(id: EntityID<Int>) : IntEntity(id) {
    var kanji by KanjiTable.kanji
    var strokeCount by KanjiTable.strokeCount
    var interpretation by KanjiTable.interpretation
    var frequency by KanjiTable.frequency
    var grade by KanjiTable.grade
    var svg by KanjiTable.svg
    var jlptLevel by KanjiTable.jlptLevel

    fun updateWithModel(model: KanjiModel) {
        kanji = model.kanji
        strokeCount = model.strokeCount
        interpretation = model.interpretation
        frequency = model.frequency
        grade = model.grade
        svg = model.svg
        jlptLevel = JlptLevel.fromStr(model.jlptLevel).code
    }

    companion object : IntEntityClass<KanjiEntity>(KanjiTable)
}