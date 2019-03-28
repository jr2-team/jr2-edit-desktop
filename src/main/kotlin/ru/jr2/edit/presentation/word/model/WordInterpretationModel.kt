package ru.jr2.edit.presentation.word.model

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import ru.jr2.edit.domain.entity.WordInterpEntity
import ru.jr2.edit.presentation.base.model.BaseModel
import tornadofx.getValue
import tornadofx.setValue

class WordInterpretationModel(id: Int = 0) : BaseModel(id) {
    val pInterpretation = SimpleStringProperty()
    var interpretation: String by pInterpretation

    val pPos = SimpleStringProperty()
    var pos: String by pPos

    val pLanguage = SimpleStringProperty()
    var language: String by pLanguage

    val pWord = SimpleIntegerProperty()
    var word: Int by pWord

    companion object {
        fun fromEntity(entity: WordInterpEntity): WordInterpretationModel {
            return WordInterpretationModel(entity.id.value).apply {
                interpretation = entity.interpretation
                pos = entity.pos
                language = entity.language
                word = entity.word.value
            }
        }
    }
}