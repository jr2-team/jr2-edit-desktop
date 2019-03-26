package ru.jr2.edit.presentation.kanji.viewmodel.list

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.stage.StageStyle
import kotlinx.coroutines.launch
import ru.jr2.edit.domain.dto.KanjiDto
import ru.jr2.edit.domain.usecase.KanjiDbUseCase
import ru.jr2.edit.presentation.kanji.model.KanjiModel
import ru.jr2.edit.presentation.kanji.view.edit.KanjiEditFragment
import ru.jr2.edit.presentation.kanji.view.parser.KanjiParserFragment
import ru.jr2.edit.presentation.base.viewmodel.BaseEditViewModel
import ru.jr2.edit.presentation.base.viewmodel.CoroutineViewModel

class KanjiListViewModel(
    private val kanjiDbUseCase: KanjiDbUseCase = KanjiDbUseCase()
) : CoroutineViewModel() {
    private var selectedKanjiId: Int = 0

    val kanjis: ObservableList<KanjiDto> = FXCollections.observableArrayList<KanjiDto>()
    val components: ObservableList<KanjiModel> = FXCollections.observableArrayList<KanjiModel>()

    init {
        subscribe<BaseEditViewModel.ItemSavedEvent> { ctx ->
            if (ctx.isSaved) loadContent()
        }
    }

    fun loadContent() = launch {
        kanjis.clear()
        kanjis.addAll(kanjiDbUseCase.getAllKanjiWithReadings())
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

    fun onParseClick() = find<KanjiParserFragment>()
        .openModal(StageStyle.UTILITY, escapeClosesWindow = false, resizable = false)
}