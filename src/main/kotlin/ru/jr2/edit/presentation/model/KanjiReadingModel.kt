package ru.jr2.edit.presentation.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import ru.jr2.edit.domain.entity.KanjiReadingEntity
import ru.jr2.edit.domain.misc.KanjiReadingType
import tornadofx.getValue
import tornadofx.setValue

class KanjiReadingModel(id: Int = 0) : BaseModel(id) {
    val pReading = SimpleStringProperty()
    var reading: String by pReading

    val pReadingType = SimpleStringProperty()
    var readingType: String by pReadingType

    val pPriority = SimpleIntegerProperty()
    var priority: Int  by pPriority

    val pIsAnachronism = SimpleBooleanProperty()
    var isAnachronism: Boolean by pIsAnachronism

    val pKanji = SimpleIntegerProperty()
    var kanji: Int = 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as KanjiReadingModel
        return other.id == id && other.reading == reading
    }

    // auto-generated
    override fun hashCode(): Int {
        var result = pReading.hashCode()
        result = 31 * result + pReadingType.hashCode()
        result = 31 * result + pPriority.hashCode()
        result = 31 * result + pIsAnachronism.hashCode()
        result = 31 * result + pKanji.hashCode()
        result = 31 * result + kanji
        return result
    }

    companion object {
        fun fromEntity(readingEntity: KanjiReadingEntity): KanjiReadingModel {
            return KanjiReadingModel(readingEntity.id.value).apply {
                reading = readingEntity.reading
                readingType = KanjiReadingType.fromCode(readingEntity.readingType).str
                priority = readingEntity.priority
                isAnachronism = readingEntity.isAnachronism
                kanji = readingEntity.kanji.value
            }
        }
    }
}