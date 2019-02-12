package ru.jr2.edit.domain.model

import javafx.beans.property.SimpleStringProperty
import ru.jr2.edit.domain.entity.SentenceEntity
import tornadofx.getValue
import tornadofx.setValue

class Sentence(
    id: Int = 0,
    value: String = String(),
    furigana: String = String(),
    interpretation: String = String()
) : BaseModel(id, value) {
    val pFurigana = SimpleStringProperty(furigana)
    var furigana: String by pFurigana

    val pInterpretation = SimpleStringProperty(interpretation)
    var interpretation: String by pInterpretation

    companion object {
        fun fromEntity(sentenceEntity: SentenceEntity): Sentence = with(sentenceEntity) {
            Sentence(
                id.value,
                value,
                furigana ?: String(),
                interpretation ?: String()
            )
        }
    }
}