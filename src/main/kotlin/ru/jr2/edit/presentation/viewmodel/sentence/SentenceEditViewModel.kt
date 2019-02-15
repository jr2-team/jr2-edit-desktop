package ru.jr2.edit.presentation.viewmodel.sentence

import ru.jr2.edit.data.db.repository.SentenceDbRepository
import ru.jr2.edit.domain.model.Sentence
import ru.jr2.edit.presentation.viewmodel.BaseEditViewModel

class SentenceEditViewModel(
    sentenceId: Int
) : BaseEditViewModel<Sentence>(sentenceId, SentenceDbRepository(), Sentence()) {
    val pValue = bind(Sentence::pValue)
    val pFurigana = bind(Sentence::pFurigana)
    val pInterpretation = bind(Sentence::pInterpretation)
}