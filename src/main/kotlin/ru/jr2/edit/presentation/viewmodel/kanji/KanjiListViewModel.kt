package ru.jr2.edit.presentation.viewmodel.kanji

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.stage.StageStyle
import ru.jr2.edit.data.db.repository.KanjiDbRepository
import ru.jr2.edit.domain.model.Kanji
import ru.jr2.edit.domain.usecase.KanjiUseCase
import ru.jr2.edit.presentation.view.kanji.KanjiParseFragment
import ru.jr2.edit.presentation.view.kanji.edit.KanjiEditFragment
import ru.jr2.edit.presentation.viewmodel.BaseEditViewModel
import tornadofx.ViewModel

class KanjiListViewModel(
    private val kanjiRepository: KanjiDbRepository = KanjiDbRepository(),
    private val kanjiUseCase: KanjiUseCase = KanjiUseCase()
) : ViewModel() {
    val kanjis: ObservableList<Kanji> = FXCollections.observableArrayList<Kanji>()
    val components: ObservableList<Kanji> = FXCollections.observableArrayList<Kanji>()
    var selectedKanji: Kanji? = null

    init {
        subscribe<BaseEditViewModel.ItemSavedEvent> { ctx ->
            if (ctx.isSaved) loadContent()
        }
    }

    fun loadContent() {
        kanjis.clear()
        kanjis.addAll(kanjiUseCase.getAllKanjiWithReadings())
    }

    fun onKanjiSelect(kanji: Kanji) {
        components.clear()
        components.addAll(kanjiRepository.getComponentsOfKanji(kanji.id))
    }

    fun onNewKanjiClick() {
        find<KanjiEditFragment>().openModal(
            StageStyle.UTILITY,
            escapeClosesWindow = false,
            resizable = false
        )
    }

    fun onEditKanjiClick() {
        find<KanjiEditFragment>(
            Pair(KanjiEditFragment::paramItemId, selectedKanji?.id)
        ).openModal(
            StageStyle.UTILITY,
            escapeClosesWindow = false,
            resizable = false
        )
    }

    fun onDeleteKanjiClick() {
        selectedKanji?.let {
            kanjiRepository.delete(it)
            kanjis.remove(it)
        }
    }

    fun onParseClick() {
        find<KanjiParseFragment>().openModal(
            StageStyle.UTILITY,
            escapeClosesWindow = false,
            resizable = false
        )
    }
}