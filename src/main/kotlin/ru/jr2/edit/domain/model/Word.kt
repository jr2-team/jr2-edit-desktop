package ru.jr2.edit.domain.model

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import ru.jr2.edit.domain.entity.WordEntity

data class Word(
    var id: Int = 0,
    var value: String = "",
    var furigana: String = "",
    var basicInterpretation: String = "",
    var jlptLevel: Int = 5
) {
    val idProp = SimpleIntegerProperty(this, "id", id)

    val valueProp = SimpleStringProperty(this, "value", value)

    val furiganaProp = SimpleStringProperty(this, "furigana", furigana)

    val basicInterpretationProp = SimpleStringProperty(
        this,
        "basicInterpretation",
        basicInterpretation
    )

    val jlptLevelProp = SimpleIntegerProperty(this, "jlpt", jlptLevel)

    companion object {
        fun fromEntity(wordEntity: WordEntity) = with(wordEntity) {
            Word(
                id.value,
                value,
                furigana ?: "",
                basicInterpretation ?: "",
                jlptLevel ?: 5
            )
        }
    }
}