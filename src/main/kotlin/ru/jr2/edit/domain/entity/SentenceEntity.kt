package ru.jr2.edit.domain.entity

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import ru.jr2.edit.data.db.table.SentenceTable

class SentenceEntity(id: EntityID<Int>) : IntEntity(id) {
    var sentence by SentenceTable.sentence
    var furigana by SentenceTable.furigana
    var interpretation by SentenceTable.interpretation

    companion object : IntEntityClass<SentenceEntity>(SentenceTable)
}