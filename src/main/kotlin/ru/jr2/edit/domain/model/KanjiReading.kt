package ru.jr2.edit.domain.model

import ru.jr2.edit.domain.entity.KanjiReadingEntity

class KanjiReading(
    id: Int = 0,
    var reading: String,
    var readingType: Int,
    var priority: Int,
    var isAnachronism: Boolean = false,
    var kanji: Int
) : BaseModel(id) {
    companion object {
        fun fromEntity(entity: KanjiReadingEntity): KanjiReading {
            return KanjiReading(
                entity.id.value,
                entity.reading,
                entity.readingType,
                entity.priority,
                entity.isAnachronism,
                entity.kanji.value
            )
        }
    }
}