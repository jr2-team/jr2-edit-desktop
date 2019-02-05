package ru.jr2.edit.presentation.viewmodel.word.edit

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import ru.jr2.edit.data.db.repository.WordDbRepository
import ru.jr2.edit.domain.model.Word
import tornadofx.ViewModel
import kotlin.coroutines.CoroutineContext

class WordEditViewModel(
    wordId: Int,
    private val mode: WordEditMode = if (wordId == 0) WordEditMode.CREATE else WordEditMode.UPDATE,
    private val wordRepository: WordDbRepository = WordDbRepository()
) : ViewModel(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    var observableWord = Word()
        private set

    /*var observableValidationState: ObservableValue<Boolean>
        private set
        get() {
            return validateWord()
        }*/

    init {
        if (mode == WordEditMode.UPDATE) {
            observableWord = wordRepository.getById(wordId)
        }
    }

    fun onWordSave() {
        if (validateWord()) {
            when (mode) {
                WordEditMode.UPDATE -> wordRepository.update(observableWord)
                WordEditMode.CREATE -> wordRepository.create(observableWord)
            }
            fire(WordSavedEvent(observableWord.toString()))
        }
    }

    private fun validateWord(): Boolean {
        var isValid = true
        if (observableWord.value.isEmpty()) {
            isValid = false
        }
        return isValid
    }
}
