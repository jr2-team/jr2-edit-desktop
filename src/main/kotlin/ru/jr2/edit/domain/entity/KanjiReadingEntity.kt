package ru.jr2.edit.domain.entity

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import ru.jr2.edit.data.db.table.KanjiReadingTable

class KanjiReadingEntity(id: EntityID<Int>) : IntEntity(id) {
    val reading by KanjiReadingTable.reading
    val readingType by KanjiReadingTable.readingType
    val priority by KanjiReadingTable.priority
    val isAnachronism by KanjiReadingTable.isAnachronism
    val kanji by KanjiReadingTable.kanji
}