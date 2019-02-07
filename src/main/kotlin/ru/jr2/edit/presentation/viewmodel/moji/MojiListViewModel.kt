package ru.jr2.edit.presentation.viewmodel.moji

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.stage.StageStyle
import ru.jr2.edit.data.db.repository.MojiDbRepository
import ru.jr2.edit.domain.model.Moji
import ru.jr2.edit.presentation.view.moji.MojiEditFragment
import tornadofx.ViewModel

class MojiListViewModel(
    private val mojiRepository: MojiDbRepository = MojiDbRepository()
) : ViewModel() {
    val mojis: ObservableList<Moji> = FXCollections.observableArrayList<Moji>()
    val components: ObservableList<Moji> = FXCollections.observableArrayList<Moji>()
    var selectedMoji: Moji? = null

    init {
        fetchContent()
    }

    fun onMojiSelect(kanji: Moji) {
        components.clear()
        components.addAll(mojiRepository.getComponentsOfKanji(kanji.id))
        selectedMoji = kanji
    }

    fun onShowNewMojiFragment() {
        find<MojiEditFragment>().openModal(StageStyle.UTILITY, resizable = false)
    }

    fun onShowEditMojiFragment() {
        find<MojiEditFragment>(
            Pair(MojiEditFragment::mojiIdParam, selectedMoji?.id)
        ).openModal(StageStyle.UTILITY, resizable = false)
    }

    private fun fetchContent() {
        mojis.clear()
        mojis.addAll(mojiRepository.getAll())
    }
}