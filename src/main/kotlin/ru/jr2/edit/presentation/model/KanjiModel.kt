package ru.jr2.edit.presentation.model

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
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as KanjiModel
        return other.id == id
    }

    // auto-generated
    override fun hashCode(): Int {
        var result = pKanji.hashCode()
        result = 31 * result + pStrokeCount.hashCode()
        result = 31 * result + pInterpretation.hashCode()
        result = 31 * result + pFrequency.hashCode()
        result = 31 * result + pGrade.hashCode()
        result = 31 * result + pJlptLevel.hashCode()
        result = 31 * result + (svg?.hashCode() ?: 0)
        return result
    }

    companion object {
        fun fromEntity(kanjiEntity: KanjiEntity): KanjiModel {
            return KanjiModel(kanjiEntity.id.value).apply {
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
}