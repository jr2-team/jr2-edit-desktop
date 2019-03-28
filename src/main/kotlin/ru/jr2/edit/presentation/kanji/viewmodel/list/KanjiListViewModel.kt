package ru.jr2.edit.presentation.kanji.viewmodel.list

import javafx.beans.property.SimpleIntegerProperty
import javafx.collections.FXCollections
import javafx.stage.StageStyle
import kotlinx.coroutines.launch
import ru.jr2.edit.domain.dto.KanjiDto
import ru.jr2.edit.domain.usecase.KanjiDbUseCase
import ru.jr2.edit.presentation.base.viewmodel.BaseEditViewModel
import ru.jr2.edit.presentation.base.viewmodel.CoroutineViewModel
import ru.jr2.edit.presentation.kanji.model.KanjiModel
import ru.jr2.edit.presentation.kanji.view.edit.KanjiEditFragment
import ru.jr2.edit.presentation.kanji.view.parser.KanjiParserFragment
import tornadofx.getValue
import tornadofx.onChange
import tornadofx.setValue
import kotlin.math.ceil

class KanjiListViewModel(
    private val kanjiDbUseCase: KanjiDbUseCase = KanjiDbUseCase()
) : CoroutineViewModel() {
    private var selectedKanjiId: Int = 0

    val kanjis = FXCollections.observableArrayList<KanjiDto>()
    val components = FXCollections.observableArrayList<KanjiModel>()

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
        totalPageCount = ceil(18000 / KANJIS_A_PAGE.toDouble()).toInt()
        kanjis.clear()
        kanjis.addAll(kanjiDbUseCase.getAllKanjiWithReadings(KANJIS_A_PAGE, (currentPage - 1) * 100))
    }

    fun onKanjiSelectChange(kanjiId: Int, needToLoadComponents: Boolean = false) {
        selectedKanjiId = kanjiId
        if (needToLoadComponents) {
            components.clear()
            components.addAll(kanjiDbUseCase.getKanjiComponents(kanjiId))
        }
    }

    fun onNewKanjiClick() = find<KanjiEditFragment>()
        .openModal(StageStyle.UTILITY, escapeClosesWindow = false, resizable = false)

    fun onEditKanjiClick() = find<KanjiEditFragment>(
        Pair(KanjiEditFragment::paramItemId, selectedKanjiId)
    ).openModal(StageStyle.UTILITY, escapeClosesWindow = false, resizable = false)

    fun onDeleteKanjiClick() {
        kanjiDbUseCase.deleteKanjiWithComponentsAndReadings(selectedKanjiId)
        kanjis.find { it.id == selectedKanjiId }?.let {
            kanjis.remove(it)
        }
    }

    fun onChangePageClick(goToTheNext: Boolean) {
        if (goToTheNext) {
            if (currentPage < totalPageCount) currentPage++
        } else {
            if (currentPage > 1) currentPage--
        }
    }

    companion object {
        private const val KANJIS_A_PAGE = 100
    }
}