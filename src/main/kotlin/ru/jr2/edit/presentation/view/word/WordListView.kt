package ru.jr2.edit.presentation.view.word

import ru.jr2.edit.domain.model.Word
import ru.jr2.edit.presentation.viewmodel.word.list.WordListViewModel
import tornadofx.*

class WordListView : View() {
    private val viewModel: WordListViewModel by inject()

    // TODO: Перенести в .fxml
    private var btnEditWord = button("Редактировать") {
        setMinSize(120.0, 28.0)
        action { viewModel.onShowEditWordFragment() }
        isDisable = true
    }

    override val root = borderpane {
        center = tableview(viewModel.observableWords) {
            column("ID", Word::idProp)
            column("Значение", Word::valueProp)
            column("Фуригана", Word::furiganaProp)
            column("Интерпретация", Word::basicInterpretationProp)
            columnResizePolicy = SmartResize.POLICY

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
                add(btnEditWord)
            }
        }
    }
}