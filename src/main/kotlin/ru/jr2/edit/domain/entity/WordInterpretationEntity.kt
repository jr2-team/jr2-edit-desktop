package ru.jr2.edit.domain.entity

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import ru.jr2.edit.data.db.table.WordInterpretationTable

class WordInterpretationEntity(id: EntityID<Int>) : IntEntity(id) {
    var interpretation by WordInterpretationTable.interpretation
    var pos by WordInterpretationTable.pos
    var language by WordInterpretationTable.language
    var word by WordInterpretationTable.word

    companion object : IntEntityClass<WordInterpretationEntity>(WordInterpretationTable)
}