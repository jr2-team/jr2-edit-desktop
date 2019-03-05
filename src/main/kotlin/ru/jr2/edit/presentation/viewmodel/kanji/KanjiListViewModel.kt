package ru.jr2.edit.presentation.viewmodel.kanji

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.stage.StageStyle
import ru.jr2.edit.domain.dto.KanjiDto
import ru.jr2.edit.domain.model.Kanji
import ru.jr2.edit.domain.usecase.KanjiDbUseCase
import ru.jr2.edit.presentation.view.kanji.edit.KanjiEditFragment
import ru.jr2.edit.presentation.view.kanji.parser.KanjiParserFragment
import ru.jr2.edit.presentation.viewmodel.BaseEditViewModel
import tornadofx.ViewModel

class KanjiListViewModel(
    private val kanjiDbUseCase: KanjiDbUseCase = KanjiDbUseCase()
) : ViewModel() {
    private var selectedKanjiId: Int = 0

    val kanjis: ObservableList<KanjiDto> = FXCollections.observableArrayList<KanjiDto>()
    val components: ObservableList<Kanji> = FXCollections.observableArrayList<Kanji>()

    init {
        subscribe<BaseEditViewModel.ItemSavedEvent> { ctx -> if (ctx.isSaved) loadContent() }
    }

    fun loadContent() {
        kanjis.clear()
        kanjis.addAll(kanjiDbUseCase.getAllKanjiWithReadings())
    }

    fun onKanjiSelect(kanjiId: Int, needToLoadComponents: Boolean = false) {
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