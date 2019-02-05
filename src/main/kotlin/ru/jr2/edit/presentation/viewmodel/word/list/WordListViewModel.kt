package ru.jr2.edit.presentation.viewmodel.word.list

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.stage.StageStyle
import org.jetbrains.exposed.sql.transactions.transaction
import ru.jr2.edit.EditApp
import ru.jr2.edit.domain.entity.WordEntity
import ru.jr2.edit.domain.model.Word
import ru.jr2.edit.presentation.view.word.WordEditFragment
import ru.jr2.edit.presentation.viewmodel.word.edit.WordSavedEvent
import tornadofx.ViewModel
import kotlin.properties.Delegates

class WordListViewModel : ViewModel() {

    private var words: List<Word> by Delegates.observable(emptyList()) { _, _, newValue ->
        observableWords.clear()
        observableWords.addAll(newValue)
    }
    val observableWords: ObservableList<Word> = FXCollections.observableArrayList<Word>()
    var selectedWord: Word? = null

    init {
        fetchWords()
        subscribeOnEventBus()
    }

    fun onShowNewWordFragment() {
        find<WordEditFragment>().openModal(stageStyle = StageStyle.UTILITY)
    }

    fun onShowEditWordFragment() {
        find<WordEditFragment>(
            Pair(WordEditFragment::wordIdParam, selectedWord?.id)
        ).openModal(stageStyle = StageStyle.UTILITY)
    }

    private fun fetchWords() {
        transaction(EditApp.instance.db) {
            words = WordEntity.all().map { Word.fromEntity(it) }
        }
    }

    private fun subscribeOnEventBus() {
        subscribe<WordSavedEvent> {
            fetchWords()
        }
    }
}