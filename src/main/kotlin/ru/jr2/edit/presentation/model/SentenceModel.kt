package ru.jr2.edit.presentation.model

import javafx.beans.property.SimpleStringProperty
import ru.jr2.edit.domain.entity.SentenceEntity
import tornadofx.getValue
import tornadofx.setValue

class SentenceModel(id: Int = 0) : BaseModel(id) {
    val pSentence = SimpleStringProperty()
    var sentence: String by pSentence

    val pFurigana = SimpleStringProperty()
    var furigana: String? by pFurigana

    val pInterpretation = SimpleStringProperty()
    var interpretation: String? by pInterpretation

    companion object {
        fun fromEntity(sentenceEntity: SentenceEntity): SentenceModel =
            SentenceModel(sentenceEntity.id.value).apply {
                sentence = sentenceEntity.sentence
                furigana = sentenceEntity.furigana
                interpretation = sentenceEntity.interpretation
            }
    }
}