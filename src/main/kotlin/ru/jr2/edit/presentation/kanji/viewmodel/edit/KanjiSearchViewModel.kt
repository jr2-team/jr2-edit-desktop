package ru.jr2.edit.presentation.kanji.viewmodel.edit

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import ru.jr2.edit.data.db.repository.KanjiDbRepository
import ru.jr2.edit.presentation.kanji.model.KanjiModel
import tornadofx.ViewModel

class KanjiSearchViewModel(
    private val kanjiRepository: KanjiDbRepository = KanjiDbRepository()
) : ViewModel() {
    val kanjis: ObservableList<KanjiModel> = FXCollections.observableArrayList<KanjiModel>().also {
        it.addAll(kanjiRepository.getAll())
    }

    fun onSearchQueryChanged(query: String) {
        kanjis.clear()
        kanjis.addAll(
            if (!query.isBlank()) {
                kanjiRepository.getBySearchQuery(query)
            } else {
                kanjiRepository.getAll()
            }
        )
    }
}