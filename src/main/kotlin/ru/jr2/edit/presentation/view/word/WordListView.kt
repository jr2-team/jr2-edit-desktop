package ru.jr2.edit.presentation.view.word

import javafx.scene.control.Button
import ru.jr2.edit.Style
import ru.jr2.edit.domain.model.Word
import ru.jr2.edit.presentation.viewmodel.word.WordListViewModel
import ru.jr2.edit.util.showWarningMsg
import tornadofx.*

class WordListView : View() {
    private val viewModel: WordListViewModel by inject()

    private var btnEditWord: Button by singleAssign()
    private var btnDeleteWord: Button by singleAssign()

    override val root = borderpane {
        center = tableview(viewModel.words) {
            column("Слово", Word::pValue)
            column("Фуригана", Word::pFurigana)
            column("Интерпретация", Word::pInterpretation).remainingWidth()
            column("Уровень JLPT", Word::pJlptLevel)
            smartResize()
            onSelectionChange { word ->
                (word !is Word).let {
                    btnEditWord.isDisable = it
                    btnDeleteWord.isDisable = it
                }
                viewModel.selectedWord = word
            }
            onUserSelect(2) { viewModel.onEditWordClick() }
        }

        bottom = borderpane {
            left = buttonbar {
                button("Добавить").action { viewModel.onNewWordClick() }
                btnEditWord = button("Редактировать") {
                    isDisable = true
                    action { viewModel.onEditWordClick() }
                }
                btnDeleteWord = button("Удалить") {
                    isDisable = true
                    action { showDeleteWordWarning() }
                }
            }

            addClass(Style.bottomButtonPane)
        }
    }

    private fun showDeleteWordWarning() = showWarningMsg(
        "Удалить слово",
        "Вы уверены, что хотите удалить ${viewModel.selectedWord.toString()}?",
        viewModel::onDeleteWordClick
    )
}