package ru.jr2.edit.presentation.viewmodel.word.edit

import ru.jr2.edit.data.db.repository.WordDbRepository
import ru.jr2.edit.domain.model.Word
import tornadofx.ItemViewModel

class WordEditItemViewModel(
    val wordId: Int,
    private val mode: WordEditMode = if (wordId == 0) WordEditMode.CREATE else WordEditMode.UPDATE,
    private val wordRepository: WordDbRepository = WordDbRepository()
) : ItemViewModel<Word>() {
    var value = bind(Word::valueProp)
    val furigana = bind(Word::furiganaProp)
    val basicInterpretation = bind(Word::basicInterpretationProp)
    val jlptLevel = bind(Word::jlptLevelProp)

    init {
        item = when (mode) {
            WordEditMode.UPDATE -> wordRepository.getById(wordId)
            WordEditMode.CREATE -> Word()
        }
    }

    fun onWordSave() {
        if (this.isValid) {
            when (mode) {
                WordEditMode.UPDATE -> wordRepository.update(item)
                WordEditMode.CREATE -> wordRepository.create(item)
            }
            fire(WordSavedEvent(item.toString()))
        }
    }
}
