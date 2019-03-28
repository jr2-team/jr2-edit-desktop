package ru.jr2.edit.presentation.word.viewmodel.edit

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import ru.jr2.edit.data.db.repository.WordDbRepository
import ru.jr2.edit.data.db.repository.WordInterpDbRepository
import ru.jr2.edit.presentation.base.viewmodel.BaseEditViewModel
import ru.jr2.edit.presentation.word.model.WordInterpretationModel
import ru.jr2.edit.presentation.word.model.WordModel
import ru.jr2.edit.util.EditMode

class WordEditViewModel(
    wordId: Int,
    private val wordInterpDbRepo: WordInterpDbRepository = WordInterpDbRepository()
) : BaseEditViewModel<WordModel>(
    wordId, WordDbRepository(),
    WordModel()
) {
    val pWord = bind(WordModel::pWord)
    val pFurigana = bind(WordModel::pFurigana)
    val pJlptLevel = bind(WordModel::pJlptLevel)

    val wordInterps: ObservableList<WordInterpretationModel> =
        FXCollections.observableArrayList()

    init {
        if (mode == EditMode.UPDATE) {
            wordInterps.addAll(wordInterpDbRepo.getByWordId(wordId))
        }
    }
}