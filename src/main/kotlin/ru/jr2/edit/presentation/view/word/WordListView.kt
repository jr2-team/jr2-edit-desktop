package ru.jr2.edit.presentation.view.word

import javafx.scene.control.Button
import ru.jr2.edit.domain.model.Word
import ru.jr2.edit.presentation.viewmodel.word.WordListViewModel
import tornadofx.*

class WordListView : View() {
    private val viewModel: WordListViewModel by inject()

    private var btnEditWord: Button by singleAssign()

    override val root = borderpane {
        center = tableview(viewModel.observableWords) {
            column("ID", Word::idProp)
            column("Значение", Word::valueProp)
            column("Фуригана", Word::furiganaProp)
            column("Интерпретация", Word::basicInterpretationProp)
            smartResize()
            onSelectionChange { word ->
                btnEditWord.isDisable = word == null
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
                    action { viewModel.onShowEditWordFragment() }
                    isDisable = true
                }
            }
        }
    }
}