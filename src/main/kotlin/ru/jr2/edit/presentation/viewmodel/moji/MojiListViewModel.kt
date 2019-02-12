package ru.jr2.edit.presentation.viewmodel.moji

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.stage.StageStyle
import ru.jr2.edit.data.db.repository.MojiDbRepository
import ru.jr2.edit.domain.model.Moji
import ru.jr2.edit.presentation.view.moji.edit.MojiEditFragment
import tornadofx.ViewModel

class MojiListViewModel(
    private val mojiRepository: MojiDbRepository = MojiDbRepository()
) : ViewModel() {
    val mojis: ObservableList<Moji> = FXCollections.observableArrayList<Moji>()
    val components: ObservableList<Moji> = FXCollections.observableArrayList<Moji>()
    var selectedMoji: Moji? = null

    init {
        fetchContent()
        subscribeOnEventBus()
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
            Pair(MojiEditFragment::baseModelId, selectedMoji?.id)
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

    private fun fetchContent() {
        mojis.clear()
        mojis.addAll(mojiRepository.getAll())
    }

    private fun subscribeOnEventBus() {
        subscribe<MojiSavedEvent> { ctx ->
            if (ctx.isSaved) fetchContent()
        }
    }
}