package ru.jr2.edit.presentation.view.word

import javafx.scene.layout.Priority
import ru.jr2.edit.presentation.viewmodel.word.WordEditViewModel
import tornadofx.*

class WordEditFragment : Fragment() {
    private val viewModel: WordEditViewModel

    val wordIdParam: Int by param(0)

    init {
        viewModel = WordEditViewModel(wordIdParam)
        if (wordIdParam == 0) {
            this.title = "Добавить слово"
        } else {
            this.title = "Редактировать слово"
        }
    }

    override val root = borderpane {
        center = form {
            fieldset {
                field("Слово") {
                    textfield(viewModel.valueField) {
                        required(message = "Обязательное поле")
                    }
                }
                field("Фуригана") {
                    textfield(viewModel.furiganaField) {
                        required(message = "Обязательное поле")
                    }
                }
                field("Основные интерпритации") {
                    textfield(viewModel.basicInterpretationField) {
                        required(message = "Обязательное поле")
                    }
                }
                field("Уровень JLPT") {
                    textfield(viewModel.jlptLevelField) {
                        required(message = "Обязательное поле")
                        // TODO: Добавить enum class JlptLevel
                        filterInput { it.controlNewText.isInt() && it.controlNewText.toInt() in 0..5 }
                    }
                }
            }
        }
        bottom = button("Сохранить") {
            enableWhen(viewModel.valid)
            action {
                viewModel.commit {
                    viewModel.onWordSave()
                }
                close()
            }
        }
        paddingAll = 15.0
        vgrow = Priority.ALWAYS
    }
}