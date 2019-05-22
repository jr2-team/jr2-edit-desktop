package ru.jr2.edit.presentation.word.viewmodel.list

import javafx.beans.property.SimpleIntegerProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.stage.StageStyle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.jr2.edit.data.db.repository.WordDbRepository
import ru.jr2.edit.domain.dto.WordDto
import ru.jr2.edit.domain.usecase.WordDbUseCase
import ru.jr2.edit.presentation.base.viewmodel.BaseEditViewModel
import ru.jr2.edit.presentation.base.viewmodel.CoroutineViewModel
import ru.jr2.edit.presentation.word.view.WordEditFragment
import tornadofx.getValue
import tornadofx.onChange
import tornadofx.setValue
import kotlin.math.ceil

class WordListViewModel(
    private val wordRepository: WordDbRepository = WordDbRepository(),
    private val wordDbUseCase: WordDbUseCase = WordDbUseCase()
) : CoroutineViewModel() {
    val observableWords: ObservableList<WordDto> = FXCollections.observableArrayList<WordDto>()
    var selectedWord: WordDto? = null

    val pTotalPageCount = SimpleIntegerProperty(0)
    private var totalPageCount by pTotalPageCount

    val pCurrentPage = SimpleIntegerProperty(1)
    private var currentPage by pCurrentPage

    init {
        pCurrentPage.onChange {
            cancelJob()
            loadContent()
        }
        subscribe<BaseEditViewModel.ItemSavedEvent> { ctx ->
            if (ctx.isSaved) loadContent()
        }
    }

    fun loadContent() = launch {
        totalPageCount = ceil(wordRepository.getCount() / WORDS_A_PAGE.toDouble()).toInt()
        observableWords.clear()
        val wordsToAdd = withContext(Dispatchers.Default) {
            wordDbUseCase.getWordWithInterps(WORDS_A_PAGE, (currentPage - 1) * 100)
        }
        observableWords.addAll(wordsToAdd)
    }

    fun onChangePageClick(goToTheNext: Boolean) {
        if (goToTheNext) {
            if (currentPage < totalPageCount) currentPage++
        } else {
            if (currentPage > 1) currentPage--
        }
    }

    fun onNewWordClick() {
        find<WordEditFragment>().openModal(
            stageStyle = StageStyle.UTILITY,
            resizable = false,
            escapeClosesWindow = false
        )
    }

    fun onEditWordClick() {
        val paramItemIdPair = Pair(WordEditFragment::paramItemId, selectedWord?.id)
        find<WordEditFragment>(paramItemIdPair).openModal(
            stageStyle = StageStyle.UTILITY,
            resizable = false,
            escapeClosesWindow = false
        )
    }

    fun onDeleteWordClick() {
        selectedWord?.let {
            //wordRepository.delete(it)
            loadContent()
        }
    }

    companion object {
        private const val WORDS_A_PAGE = 100
    }
}