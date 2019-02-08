package ru.jr2.edit.presentation.viewmodel.word

import ru.jr2.edit.data.db.repository.WordDbRepository
import ru.jr2.edit.domain.model.Word
import ru.jr2.edit.presentation.viewmodel.EditMode
import tornadofx.ItemViewModel

class WordEditViewModel(
    wordId: Int,
    private val mode: EditMode = if (wordId == 0) EditMode.CREATE else EditMode.UPDATE,
    private val wordRepository: WordDbRepository = WordDbRepository()
) : ItemViewModel<Word>() {
    val valueField = bind(Word::value)
    val furiganaField = bind(Word::furiganaProp)
    val basicInterpretationField = bind(Word::basicInterpretationProp)
    val jlptLevelField = bind(Word::jlptLevelProp)

    init {
        item = when (mode) {
            EditMode.UPDATE -> wordRepository.getById(wordId)
            EditMode.CREATE -> Word()
        }
    }

    fun onSaveClick() {
        if (this.isValid) {
            when (mode) {
                EditMode.UPDATE -> wordRepository.insertUpdate(item)
                EditMode.CREATE -> wordRepository.insert(item)
            }
            fire(WordSavedEvent(item.toString()))
        }
    }
}
