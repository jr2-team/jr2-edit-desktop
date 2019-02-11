package ru.jr2.edit.presentation.view.word

import javafx.scene.layout.Priority
import ru.jr2.edit.presentation.viewmodel.word.WordEditViewModel
import tornadofx.*

class WordEditFragment : Fragment() {
    private val viewModel: WordEditViewModel

    val paramWordId: Int by param(0)

    init {
        viewModel = WordEditViewModel(paramWordId)
        title = if (paramWordId == 0) "Добавить слово" else "Редактировать слово"
    }

    override val root = borderpane {
        center = form {
            fieldset {
                field("Слово") {
                    textfield(viewModel.pValue) {
                        required(message = "Обязательное поле")
                    }
                }
                field("Фуригана") {
                    textfield(viewModel.pFurigana)
                }
                field("Основные интерпритации") {
                    textfield(viewModel.pInterpretation) {
                        required(message = "Обязательное поле")
                    }
                }
                field("Уровень JLPT") {
                    textfield(viewModel.pJlptLevel) {
                        required(message = "Обязательное поле")
                        filterInput {
                            with(it.controlNewText) {
                                return@filterInput isInt() && toInt() in 0..5
                            }
                        }
                    }
                }

            }
        }
        bottom = button("Сохранить") {
            setMinSize(120.0, 28.0)
            enableWhen(viewModel.valid)
            action {
                viewModel.commit { viewModel.onSaveClick() }
                close()
            }
        }
        paddingAll = 15.0
        vgrow = Priority.ALWAYS
    }
}