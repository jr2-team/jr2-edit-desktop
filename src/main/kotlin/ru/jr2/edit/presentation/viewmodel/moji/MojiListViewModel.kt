package ru.jr2.edit.presentation.viewmodel.moji

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.stage.StageStyle
import ru.jr2.edit.data.db.repository.MojiDbRepository
import ru.jr2.edit.domain.model.Moji
import ru.jr2.edit.presentation.view.moji.KanjiParseFragment
import ru.jr2.edit.presentation.view.moji.edit.MojiEditFragment
import ru.jr2.edit.presentation.viewmodel.BaseEditViewModel
import tornadofx.ViewModel

class MojiListViewModel(
    private val mojiRepository: MojiDbRepository = MojiDbRepository()
) : ViewModel() {
    val mojis: ObservableList<Moji> = FXCollections.observableArrayList<Moji>()
    val components: ObservableList<Moji> = FXCollections.observableArrayList<Moji>()
    var selectedMoji: Moji? = null

    init {
        subscribe<BaseEditViewModel.ItemSavedEvent> { ctx ->
            if (ctx.isSaved) loadContent()
        }
    }

    fun loadContent() {
        mojis.clear()
        mojis.addAll(mojiRepository.getAll())
    }

    fun onMojiSelect(kanji: Moji) {
        components.clear()
        components.addAll(mojiRepository.getComponentsOfMoji(kanji.id))
    }

    fun onNewMojiClick() {
        find<MojiEditFragment>().openModal(
            StageStyle.UTILITY,
            escapeClosesWindow = false,
            resizable = false
        )
    }

    fun onEditMojiClick() {
        find<MojiEditFragment>(
            Pair(MojiEditFragment::paramItemId, selectedMoji?.id)
        ).openModal(
            StageStyle.UTILITY,
            escapeClosesWindow = false,
            resizable = false
        )
    }

    fun onDeleteMojiClick() {
        selectedMoji?.let {
            mojiRepository.delete(it)
            mojis.remove(it)
        }
    }

    // TODO: Добавить фильтрацию
    fun onFilterMojiType(mojiType: Int = -1) {

    }

    fun onParseClick() {
        find<KanjiParseFragment>().openModal(
            StageStyle.UTILITY,
            escapeClosesWindow = false,
            resizable = false
        )
    }
}