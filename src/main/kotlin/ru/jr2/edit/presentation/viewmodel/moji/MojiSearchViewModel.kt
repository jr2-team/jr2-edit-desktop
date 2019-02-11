package ru.jr2.edit.presentation.viewmodel.moji

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import ru.jr2.edit.data.db.repository.MojiDbRepository
import ru.jr2.edit.domain.model.Moji
import tornadofx.ViewModel

class MojiSearchViewModel(
    private val mojiRepository: MojiDbRepository = MojiDbRepository()
) : ViewModel() {
    val mojis: ObservableList<Moji> = FXCollections.observableArrayList<Moji>().also {
        it.addAll(mojiRepository.getAll())
    }

    fun onSearchQueryChanged(query: String) {
        mojis.clear()
        mojis.addAll(
            if (!query.isBlank()) {
                mojiRepository.getBySearchQuery(query)
            } else {
                mojiRepository.getAll()
            }
        )
    }
}