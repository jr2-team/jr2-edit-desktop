package ru.jr2.edit.presentation.view.kanji

import ru.jr2.edit.Style
import ru.jr2.edit.presentation.viewmodel.kanji.KanjiParseViewModel
import tornadofx.*

class KanjiParseFragment : Fragment("Парсинг Edict-словаря канджи") {
    val viewModel: KanjiParseViewModel by inject()

    override val root = form {
        borderpane {
            top = label(viewModel.pProcessingStateMsg)

            bottom = buttonbar {
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

                paddingTop = 10.0
            }

            paddingAll = 10.0
        }
    }
}