package ru.jr2.edit.presentation.word.viewmodel.edit

import ru.jr2.edit.data.db.repository.WordDbRepository
import ru.jr2.edit.presentation.word.model.WordModel
import ru.jr2.edit.presentation.base.viewmodel.BaseEditViewModel

class WordEditViewModel(
    wordId: Int
) : BaseEditViewModel<WordModel>(wordId, WordDbRepository(),
    WordModel()
) {
    val pWord = bind(WordModel::pWord)
    val pFurigana = bind(WordModel::pFurigana)
    val pInterpretation = bind(WordModel::pInterpretation)
    val pJlptLevel = bind(WordModel::pJlptLevel)
}
