package ru.jr2.edit.presentation.viewmodel.word

import ru.jr2.edit.data.db.repository.WordDbRepository
import ru.jr2.edit.domain.model.Word
import ru.jr2.edit.presentation.viewmodel.BaseEditViewModel
import ru.jr2.edit.presentation.viewmodel.EditMode

class WordEditViewModel(
    wordId: Int
) : BaseEditViewModel<Word>(wordId, WordDbRepository(), Word()) {
    val pValue = bind(Word::pValue)
    val pFurigana = bind(Word::pFurigana)
    val pInterpretation = bind(Word::pInterpretation)
    val pJlptLevel = bind(Word::pJlptLevel)
}
