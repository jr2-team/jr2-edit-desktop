package ru.jr2.edit.presentation.sentence.viewmodel

import ru.jr2.edit.data.db.repository.SentenceDbRepository
import ru.jr2.edit.presentation.sentence.model.SentenceModel
import ru.jr2.edit.presentation.base.viewmodel.BaseEditViewModel

class SentenceEditViewModel(
    sentenceId: Int
) : BaseEditViewModel<SentenceModel>(sentenceId, SentenceDbRepository(),
    SentenceModel()
) {
    val pSentence = bind(SentenceModel::pSentence)
    val pFurigana = bind(SentenceModel::pFurigana)
    val pInterpretation = bind(SentenceModel::pInterpretation)
}