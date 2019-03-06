package ru.jr2.edit.presentation.viewmodel.sentence

import ru.jr2.edit.data.db.repository.SentenceDbRepository
import ru.jr2.edit.presentation.model.SentenceModel
import ru.jr2.edit.presentation.viewmodel.BaseEditViewModel

class SentenceEditViewModel(
    sentenceId: Int
) : BaseEditViewModel<SentenceModel>(sentenceId, SentenceDbRepository(), SentenceModel()) {
    val pSentence = bind(SentenceModel::pSentence)
    val pFurigana = bind(SentenceModel::pFurigana)
    val pInterpretation = bind(SentenceModel::pInterpretation)
}