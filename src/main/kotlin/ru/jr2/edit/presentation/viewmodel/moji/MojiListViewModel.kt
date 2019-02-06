package ru.jr2.edit.presentation.viewmodel.moji

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import ru.jr2.edit.data.db.repository.MojiDbRepository
import ru.jr2.edit.domain.model.Moji
import tornadofx.ViewModel

class MojiListViewModel(
    private val mojiRepository: MojiDbRepository = MojiDbRepository()
) : ViewModel() {
    val mojis: ObservableList<Moji> = FXCollections.observableArrayList<Moji>()
    val components: ObservableList<Moji> = FXCollections.observableArrayList<Moji>()

    init {
        fetchContent()
    }

    fun onKanjiSelected(kanji: Moji) {
        components.clear()
        components.addAll(mojiRepository.getComponentsOfKanji(kanji.id))
    }

    private fun fetchContent() {
        mojis.clear()
        mojis.addAll(mojiRepository.getAll())
    }
}