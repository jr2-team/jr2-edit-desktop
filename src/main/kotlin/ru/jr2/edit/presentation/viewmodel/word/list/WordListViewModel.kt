package ru.jr2.edit.presentation.viewmodel.word.list

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.stage.StageStyle
import ru.jr2.edit.data.db.repository.WordDbRepository
import ru.jr2.edit.domain.model.Word
import ru.jr2.edit.presentation.view.word.WordEditFragment
import ru.jr2.edit.presentation.viewmodel.word.edit.WordSavedEvent
import tornadofx.ViewModel
import kotlin.properties.Delegates

class WordListViewModel(
    private val wordRepository: WordDbRepository = WordDbRepository()
) : ViewModel() {
    private var words: List<Word> by Delegates.observable(emptyList()) { _, _, words ->
        observableWords.clear()
        observableWords.addAll(words)
    }
    val observableWords: ObservableList<Word> = FXCollections.observableArrayList<Word>()
    var selectedWord: Word? = null

    init {
        fetchWords()
        subscribeOnEventBus()
    }

    fun onShowNewWordFragment() {
        find<WordEditFragment>().openModal(StageStyle.UTILITY, resizable = false)
    }

    fun onShowEditWordFragment() {
        find<WordEditFragment>(
            Pair(WordEditFragment::wordIdParam, selectedWord?.id)
        ).openModal(StageStyle.UTILITY, resizable = false)
    }

    private fun fetchWords() {
        words = wordRepository.getAll()
    }

    private fun subscribeOnEventBus() {
        subscribe<WordSavedEvent> {
            fetchWords()
        }
    }
}