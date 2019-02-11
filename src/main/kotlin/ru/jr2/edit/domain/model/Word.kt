package ru.jr2.edit.domain.model

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import ru.jr2.edit.domain.JlptLevel
import ru.jr2.edit.domain.entity.WordEntity
import tornadofx.getValue
import tornadofx.setValue

class Word(
    id: Int = 0,
    value: String = String(),
    furigana: String = String(),
    interpretation: String = String(),
    jlptLevel: String = JlptLevel.NO_LEVEL.str
) {
    val pId = SimpleIntegerProperty(id)
    var id: Int by pId

    val pValue = SimpleStringProperty(value)
    var value: String by pValue

    val pFurigana = SimpleStringProperty(furigana)
    var furigana: String by pFurigana

    val pInterpretation = SimpleStringProperty(interpretation)
    var interpretation: String by pInterpretation

    val pJlptLevel = SimpleStringProperty(jlptLevel)
    var jlptLevel: String by pJlptLevel

    companion object {
        fun fromEntity(wordEntity: WordEntity) = with(wordEntity) {
            Word(
                id.value,
                value,
                furigana ?: String(),
                interpretation ?: String(),
                JlptLevel.fromCode(jlptLevel).str
            )
        }
    }
}