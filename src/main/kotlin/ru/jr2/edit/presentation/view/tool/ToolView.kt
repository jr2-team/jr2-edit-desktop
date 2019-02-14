package ru.jr2.edit.presentation.view.tool

import ru.jr2.edit.Style
import ru.jr2.edit.Style.Companion.toolSection
import ru.jr2.edit.presentation.viewmodel.tool.ToolViewModel
import tornadofx.*


class ToolView : View() {
    private val viewModel: ToolViewModel by inject()

    override val root = borderpane {
        center = form {
            borderpane {
                top = label("Парсинг Edict-словоря слов")

                left = label(viewModel.pWordEdictFilename)

                right = buttonbar {
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
                }

                addClass(toolSection)
            }

            borderpane {
                top = label("Парсинг Edict-словоря канджи")

                left = label(viewModel.pKanjiEdictFilename)

                right = buttonbar {
                    button("Обработать файл") {
                        disableProperty().bind(viewModel.pIsBusy)
                        action {
                            viewModel.onKanjiFileChoose(chooseFile(null, emptyArray()))
                        }
                        addClass(Style.mediumButton)
                    }
                    button("Отмена") {
                        disableProperty().bind(viewModel.pIsBusy.not())
                        action { viewModel.onCancelProcessingClick() }
                        addClass(Style.mediumButton)
                    }
                }

                addClass(toolSection)
            }
        }

        bottom = label(viewModel.pWordEdictProcessingState)
    }
}