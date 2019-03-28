package ru.jr2.edit.domain.entity

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import ru.jr2.edit.data.db.table.WordInterpTable

class WordInterpEntity(id: EntityID<Int>) : IntEntity(id) {
    var interpretation by WordInterpTable.interp
    var pos by WordInterpTable.pos
    var language by WordInterpTable.language
    var word by WordInterpTable.word

    companion object : IntEntityClass<WordInterpEntity>(WordInterpTable)
}