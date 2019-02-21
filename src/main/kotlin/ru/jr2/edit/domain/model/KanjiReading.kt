package ru.jr2.edit.domain.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import ru.jr2.edit.domain.entity.KanjiReadingEntity
import tornadofx.getValue
import tornadofx.setValue

class KanjiReading(id: Int = 0) : BaseModel(id) {
    val pReading = SimpleStringProperty()
    var reading: String by pReading

    val pReadingType = SimpleIntegerProperty()
    var readingType: Int by pReadingType

    val pPriority = SimpleIntegerProperty()
    var priority: Int  by pPriority

    val pIsAnachronism = SimpleBooleanProperty()
    var isAnachronism: Boolean by pIsAnachronism

    val pKanji = SimpleIntegerProperty()
    var kanji: Int = 0

    companion object {
        fun fromEntity(readingEntity: KanjiReadingEntity): KanjiReading {
            return KanjiReading(readingEntity.id.value).apply {
                reading = readingEntity.reading
                readingType = readingEntity.readingType
                priority = readingEntity.priority
                isAnachronism = readingEntity.isAnachronism
                kanji = readingEntity.kanji.value
            }
        }
    }
}