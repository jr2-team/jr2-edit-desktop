package ru.jr2.edit.presentation.viewmodel.sentence

import ru.jr2.edit.data.db.repository.SentenceDbRepository
import ru.jr2.edit.domain.model.Sentence
import ru.jr2.edit.presentation.viewmodel.BaseEditViewModel
import ru.jr2.edit.presentation.viewmodel.EditMode

class SentenceEditViewModel(
    sentenceId: Int,
    private val sentenceRepository: SentenceDbRepository = SentenceDbRepository()
) : BaseEditViewModel<Sentence>(sentenceId, sentenceRepository, Sentence()) {
    val pValue = bind(Sentence::pValue)
    val pFurigana = bind(Sentence::pFurigana)
    val pInterpretation = bind(Sentence::pInterpretation)

    fun onSaveClick() {
        when (mode) {
            EditMode.UPDATE -> sentenceRepository.insertUpdate(item)
            EditMode.CREATE -> sentenceRepository.insert(item)
        }
        fire(SentenceSavedEvent(true))
    }
}