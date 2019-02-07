package ru.jr2.edit.presentation.view.word

import javafx.scene.layout.Priority
import ru.jr2.edit.presentation.viewmodel.word.edit.WordEditItemViewModel
import tornadofx.*

class WordEditFragment : Fragment() {
    private val itemViewModel: WordEditItemViewModel

    val wordIdParam: Int by param(0)

    init {
        itemViewModel = WordEditItemViewModel(wordIdParam)
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
                    textfield(itemViewModel.valueField) {
                        required(message = "Обязательное поле")
                    }
                }
                field("Фуригана") {
                    textfield(itemViewModel.furiganaField) {
                        required(message = "Обязательное поле")
                    }
                }
                field("Основные интерпритации") {
                    textfield(itemViewModel.basicInterpretationField) {
                        required(message = "Обязательное поле")
                    }
                }
                field("Уровень JLPT") {
                    textfield(itemViewModel.jlptLevelField) {
                        required(message = "Обязательное поле")
                        // TODO: Добавить enum class JlptLevel
                        filterInput { it.controlNewText.isInt() && it.controlNewText.toInt() in 0..5 }
                    }
                }
            }
        }
        bottom = button("Сохранить") {
            enableWhen(itemViewModel.valid)
            action {
                itemViewModel.commit {
                    itemViewModel.onWordSave()
                }
                close()
            }
        }
        paddingAll = 15.0
        vgrow = Priority.ALWAYS
    }
}