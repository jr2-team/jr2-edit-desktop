package ru.jr2.edit.presentation.viewmodel.word.edit

import ru.jr2.edit.data.db.repository.WordDbRepository
import ru.jr2.edit.presentation.model.WordModel
import ru.jr2.edit.presentation.viewmodel.BaseEditViewModel

class WordEditViewModel(
    wordId: Int
) : BaseEditViewModel<WordModel>(wordId, WordDbRepository(), WordModel()) {
    val pWord = bind(WordModel::pWord)
    val pFurigana = bind(WordModel::pFurigana)
    val pInterpretation = bind(WordModel::pInterpretation)
    val pJlptLevel = bind(WordModel::pJlptLevel)
}
