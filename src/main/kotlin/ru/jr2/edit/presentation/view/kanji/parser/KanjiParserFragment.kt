package ru.jr2.edit.presentation.view.kanji.parser

import ru.jr2.edit.Style
import ru.jr2.edit.presentation.viewmodel.kanji.parser.KanjiParseViewModel
import tornadofx.*

class KanjiParserFragment : Fragment("Парсинг Edict-словаря канджи") {
    val viewModel: KanjiParseViewModel by inject()

    override val root = form {
        borderpane {
            paddingAll = 10.0
            top = label(viewModel.pProcessingStateMsg)
            bottom = renderSelectFileCancelButtonBar()
        }
    }

    private fun renderSelectFileCancelButtonBar() = buttonbar {
        paddingTop = 10.0
        button("Обработать файл") {
            disableProperty().bind(viewModel.pIsBusy)
            action {
                viewModel.onKanjiEdictFileChoose(chooseFile(null, emptyArray()))
            }
            addClass(Style.mediumButton)
        }
        button("Отмена") {
            disableProperty().bind(viewModel.pIsBusy.not())
            action { viewModel.onCancelProcessingClick() }
            addClass(Style.mediumButton)
        }
    }
}