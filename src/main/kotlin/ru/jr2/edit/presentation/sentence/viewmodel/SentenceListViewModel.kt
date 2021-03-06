package ru.jr2.edit.presentation.sentence.viewmodel

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.stage.StageStyle
import ru.jr2.edit.data.db.repository.SentenceDbRepository
import ru.jr2.edit.presentation.sentence.model.SentenceModel
import ru.jr2.edit.presentation.sentence.view.SentenceEditFragment
import ru.jr2.edit.presentation.base.viewmodel.BaseEditViewModel
import tornadofx.ViewModel

class SentenceListViewModel(
    private val sentenceRepository: SentenceDbRepository = SentenceDbRepository()
) : ViewModel() {
    val sentences: ObservableList<SentenceModel> = FXCollections.observableArrayList<SentenceModel>()

    var selectedSentence: SentenceModel? = null

    init {
        subscribe<BaseEditViewModel.ItemSavedEvent> { ctx ->
            if (ctx.isSaved) loadContent()
        }
    }

    private fun loadContent() {
        sentences.clear()
        sentences.addAll(sentenceRepository.getAll())
    }

    fun onNewSentenceClick() {
        find<SentenceEditFragment>().openModal(
            StageStyle.UTILITY,
            escapeClosesWindow = false,
            resizable = false
        )
    }

    fun onEditSentenceClick() {
        find<SentenceEditFragment>(
            Pair(SentenceEditFragment::paramItemId, selectedSentence?.id ?: 0)
        ).openModal(
            StageStyle.UTILITY,
            escapeClosesWindow = false,
            resizable = false
        )
    }

    fun onDeleteSentenceClick() {
        selectedSentence?.let {
            sentenceRepository.delete(it)
            sentences.remove(it)
        }
    }
}