package ru.jr2.edit.domain.model

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import ru.jr2.edit.domain.entity.KanjiEntity
import ru.jr2.edit.domain.misc.JlptLevel
import tornadofx.getValue
import tornadofx.setValue

class Kanji(id: Int = 0) : BaseModel(id) {
    val pKanji = SimpleStringProperty()
    val pStrokeCount = SimpleIntegerProperty()
    val pOnReading = SimpleStringProperty()
    val pKunReading = SimpleStringProperty()
    val pInterpretation = SimpleStringProperty()
    val pFrequency = SimpleIntegerProperty()
    val pGrade = SimpleIntegerProperty()
    val pJlptLevel = SimpleStringProperty(JlptLevel.JLPT1.str)

    var kanji: String by pKanji
    var strokeCount: Int by pStrokeCount
    var onReading: String by pOnReading
    var kunReading: String by pKunReading
    var interpretation: String by pInterpretation
    var frequency: Int by pFrequency
    var grade: Int by pGrade
    var svg: String? = null
    var jlptLevel: String by pJlptLevel

    companion object {
        fun fromEntity(kanjiEntity: KanjiEntity): Kanji = Kanji(kanjiEntity.id.value).apply {
            kanji = kanjiEntity.kanji
            strokeCount = kanjiEntity.strokeCount
            interpretation = kanjiEntity.interpretation ?: String()
            frequency = kanjiEntity.frequency
            grade = kanjiEntity.grade ?: 0
            svg = kanjiEntity.svg
            // TODO: Переделать
            jlptLevel = JlptLevel.fromCode(kanjiEntity.jlptLevel).str
        }
    }
}