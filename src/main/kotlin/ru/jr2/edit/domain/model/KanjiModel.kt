package ru.jr2.edit.domain.model

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import ru.jr2.edit.domain.entity.KanjiEntity
import ru.jr2.edit.domain.misc.JlptLevel
import tornadofx.getValue
import tornadofx.setValue

class KanjiModel(id: Int = 0) : BaseModel(id) {
    val pKanji = SimpleStringProperty()
    var kanji: String by pKanji

    val pStrokeCount = SimpleIntegerProperty()
    var strokeCount: Int by pStrokeCount

    val pInterpretation = SimpleStringProperty()
    var interpretation: String? by pInterpretation

    val pFrequency = SimpleIntegerProperty()
    var frequency: Int by pFrequency

    val pGrade = SimpleIntegerProperty()
    var grade: Int by pGrade

    val pJlptLevel = SimpleStringProperty(JlptLevel.JLPT1.str)
    var jlptLevel: String by pJlptLevel

    var svg: String? = null

    override fun toString() = kanji

    override fun equals(other: Any?): Boolean {
        var areEqual = false
        if (other is KanjiModel) {
            areEqual = other.id == id
        }
        return areEqual
    }

    companion object {
        fun fromEntity(kanjiEntity: KanjiEntity) = KanjiModel(kanjiEntity.id.value).apply {
            kanji = kanjiEntity.kanji
            strokeCount = kanjiEntity.strokeCount
            interpretation = kanjiEntity.interpretation
            frequency = kanjiEntity.frequency
            grade = kanjiEntity.grade ?: 0
            svg = kanjiEntity.svg
            jlptLevel = JlptLevel.fromCode(kanjiEntity.jlptLevel).str
        }
    }
}