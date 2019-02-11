package ru.jr2.edit.presentation.view.word

import javafx.scene.control.Button
import ru.jr2.edit.domain.model.Word
import ru.jr2.edit.presentation.viewmodel.word.WordListViewModel
import tornadofx.*

class WordListView : View() {
    private val viewModel: WordListViewModel by inject()

    private var btnEditWord: Button by singleAssign()

    override val root = borderpane {
        center = tableview(viewModel.words) {
            column("ID", Word::pId)
            //column("Значение", Word::pValue)
            column("Фуригана", Word::pFurigana)
            column("Интерпретация", Word::pInterpretation)
            smartResize()
            onSelectionChange { word ->
                (word !is Word).let {
                    btnEditWord.isDisable = it
                }
                viewModel.selectedWord = word
            }
        }

        bottom = borderpane {
            paddingAll = 10.0
            right = hbox(spacing = 10) {
                button("Добавить") {
                    setMinSize(120.0, 28.0)
                    action { viewModel.onShowNewWordFragment() }
                }
                btnEditWord = button("Редактировать") {
                    setMinSize(120.0, 28.0)
                    isDisable = true
                    action { viewModel.onShowEditWordFragment() }
                }
            }
        }
    }
}