package ru.jr2.edit.domain.entity

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import ru.jr2.edit.data.db.table.WordTable

class WordEntity(id: EntityID<Int>) : IntEntity(id) {
    var word by WordTable.word
    var furigana by WordTable.furigana
    var jlptLevel by WordTable.jlptLevel

    companion object : IntEntityClass<WordEntity>(WordTable)
}