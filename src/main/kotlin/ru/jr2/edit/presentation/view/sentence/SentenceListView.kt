package ru.jr2.edit.presentation.view.sentence

import javafx.scene.control.Button
import ru.jr2.edit.Style
import ru.jr2.edit.domain.model.Sentence
import ru.jr2.edit.presentation.viewmodel.sentence.SentenceListViewModel
import ru.jr2.edit.util.showWarningMsg
import tornadofx.*

class SentenceListView : View() {
    private val viewModel: SentenceListViewModel by inject()

    private var btnEdit: Button by singleAssign()
    private var btnDelete: Button by singleAssign()

    override val root = borderpane {
        center = tableview(viewModel.sentences) {
            column("Значение", Sentence::pValue)
            column("Интерпретация", Sentence::pInterpretation).remainingWidth()
            smartResize()
            onSelectionChange { sentence ->
                (sentence !is Sentence).let {
                    btnEdit.isDisable = it
                    btnDelete.isDisable = it
                }
                sentence?.let { viewModel.selectedSentence = it }
            }
            onUserSelect(2) {
                viewModel.onEditSentenceClick()
            }
        }

        bottom = borderpane {
            left = buttonbar {
                button("Добавить") {
                    action { viewModel.onNewSentenceClick() }
                }
                btnEdit = button("Редактировать") {
                    isDisable = true
                    action { viewModel.onEditSentenceClick() }
                }
                btnDelete = button("Удалить") {
                    isDisable = true
                    action { showDeleteSentenceWarning() }
                }
            }

            addClass(Style.bottomButtonPane)
        }
    }

    private fun showDeleteSentenceWarning() = showWarningMsg(
        "Удалить предложение",
        "Вы уверены, что хотите удалить ${viewModel.selectedSentence.toString()}?",
        viewModel::onDeleteSentenceClick
    )
}