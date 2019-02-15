package ru.jr2.edit.presentation.viewmodel.word

import javafx.beans.property.SimpleIntegerProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.stage.StageStyle
import ru.jr2.edit.data.db.repository.WordDbRepository
import ru.jr2.edit.domain.model.Word
import ru.jr2.edit.presentation.view.word.WordEditFragment
import ru.jr2.edit.presentation.view.word.WordParseFragment
import ru.jr2.edit.presentation.viewmodel.BaseEditViewModel
import tornadofx.ViewModel
import tornadofx.getValue
import tornadofx.onChange
import tornadofx.setValue
import kotlin.math.ceil

class WordListViewModel(
    private val wordRepository: WordDbRepository = WordDbRepository()
) : ViewModel() {
    val words: ObservableList<Word> = FXCollections.observableArrayList<Word>()
    var selectedWord: Word? = null

    val pTotalPageCount = SimpleIntegerProperty(0)
    val pCurrentPage = SimpleIntegerProperty(1)
    private var totalPageCount by pTotalPageCount
    private var currentPage by pCurrentPage

    init {
        pCurrentPage.onChange { loadContent() }
        subscribe<BaseEditViewModel.ItemSavedEvent> { ctx ->
            if (ctx.isSaved) loadContent()
        }
    }

    fun loadContent() {
        totalPageCount = ceil(wordRepository.getCount() / WORDS_A_PAGE.toDouble()).toInt()
        words.clear()
        words.addAll(wordRepository.getWithOffset(WORDS_A_PAGE, (currentPage - 1) * 100))
    }

    fun onChangePageClick(goToTheNext: Boolean) {
        if (goToTheNext) {
            if (currentPage < totalPageCount) currentPage++
        } else {
            if (currentPage > 0) currentPage--
        }
    }

    fun onNewWordClick() {
        find<WordEditFragment>().openModal(
            StageStyle.UTILITY,
            resizable = false,
            escapeClosesWindow = false
        )
    }

    fun onEditWordClick() {
        find<WordEditFragment>(
            Pair(WordEditFragment::paramItemId, selectedWord?.id)
        ).openModal(
            StageStyle.UTILITY,
            resizable = false,
            escapeClosesWindow = false
        )
    }

    fun onDeleteWordClick() {
        selectedWord?.let {
            wordRepository.delete(it)
            loadContent()
        }
    }

    fun onParseClick() {
        find<WordParseFragment>().openModal(
            StageStyle.UTILITY,
            escapeClosesWindow = false,
            resizable = false
        )
    }

    companion object {
        private const val WORDS_A_PAGE = 100
    }
}