package ru.jr2.edit.domain.model

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import ru.jr2.edit.domain.entity.WordEntity
import tornadofx.getValue
import tornadofx.setValue

class Word(
    id: Int = 0,
    value: String = "",
    furigana: String = "",
    basicInterpretation: String = "",
    jlptLevel: Int = 5
) {
    val idProp = SimpleIntegerProperty(id)
    var id by idProp

    val valueProp = SimpleStringProperty(value)
    var value by valueProp

    val furiganaProp = SimpleStringProperty(furigana)
    var furigana by furiganaProp

    val basicInterpretationProp = SimpleStringProperty(
        this,
        "basicInterpretationField",
        basicInterpretation
    )
    var basicInterpretation: String by basicInterpretationProp

    val jlptLevelProp = SimpleIntegerProperty(jlptLevel)
    var jlptLevel by jlptLevelProp

    companion object {
        fun fromEntity(wordEntity: WordEntity) = with(wordEntity) {
            Word(
                id.value,
                value,
                furigana,
                basicInterpretation,
                jlptLevel ?: 0
            )
        }
    }
}