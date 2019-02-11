package ru.jr2.edit.domain.entity

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import ru.jr2.edit.data.db.table.WordTable

class WordEntity(id: EntityID<Int>) : IntEntity(id) {
    var value by WordTable.value
    var furigana by WordTable.furigana
    var interpretation by WordTable.interpretation
    var jlptLevel by WordTable.jlptLevel

    override fun toString(): String = "$id $value"

    companion object : IntEntityClass<WordEntity>(WordTable)
}