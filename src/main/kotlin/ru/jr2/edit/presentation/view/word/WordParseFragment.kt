package ru.jr2.edit.presentation.view.word

import ru.jr2.edit.Style
import ru.jr2.edit.presentation.viewmodel.word.WordParseViewModel
import tornadofx.*

class WordParseFragment : Fragment("Парсинг Edict-словоря слов") {
    private val viewModel: WordParseViewModel by inject()

    override val root = form {
        borderpane {
            top = label(viewModel.pProcessingStateMsg)

            bottom = buttonbar {
                button("Обработать файл") {
                    disableProperty().bind(viewModel.pIsBusy)
                    action {
                        viewModel.onWordEdictFileChoose(chooseFile(null, emptyArray()))
                    }
                    addClass(Style.mediumButton)
                }
                button("Отмена") {
                    disableProperty().bind(viewModel.pIsBusy.not())
                    action { viewModel.onCancelProcessingClick() }
                    addClass(Style.mediumButton)
                }

                paddingTop = 10.0
            }

            paddingAll = 10.0
        }
    }
}