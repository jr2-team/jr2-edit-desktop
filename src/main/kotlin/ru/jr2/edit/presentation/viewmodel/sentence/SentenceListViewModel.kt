package ru.jr2.edit.presentation.viewmodel.sentence

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.stage.StageStyle
import ru.jr2.edit.data.db.repository.SentenceDbRepository
import ru.jr2.edit.domain.model.Sentence
import ru.jr2.edit.presentation.view.sentence.SentenceEditFragment
import ru.jr2.edit.presentation.viewmodel.BaseEditViewModel
import tornadofx.ViewModel

class SentenceListViewModel(
    private val sentenceRepository: SentenceDbRepository = SentenceDbRepository()
) : ViewModel() {
    val sentences: ObservableList<Sentence> = FXCollections.observableArrayList<Sentence>()

    var selectedSentence: Sentence? = null

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