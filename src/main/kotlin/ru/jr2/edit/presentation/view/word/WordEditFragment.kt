package ru.jr2.edit.presentation.view.word

import javafx.geometry.Pos
import javafx.scene.layout.Priority
import ru.jr2.edit.presentation.viewmodel.word.edit.WordEditItemViewModel
import tornadofx.*

class WordEditFragment : Fragment("Добавить слово") {
    val wordIdParam: Int by param(0)

    private val itemViewModel: WordEditItemViewModel

    init {
        itemViewModel = WordEditItemViewModel(wordIdParam)
        if (wordIdParam == 0) {
            this.title = "Добавить слово"
        } else {
            this.title = "Редактировать слово"
        }
    }

    override val root = form {
        fieldset {
            field("Слово") {
                textfield(itemViewModel.value) {
                    required(message = "Обязательное поле")
                }
            }
            field("Фуригана") {
                textfield(itemViewModel.furigana) {
                    required(message = "Обязательное поле")
                }
            }
            field("Основные интерпритации") {
                textfield(itemViewModel.basicInterpretation) {
                    required(message = "Обязательное поле")
                }
            }
            field("Уровень JLPT") {
                textfield(itemViewModel.jlptLevel) {
                    required(message = "Обязательное поле")
                    // TODO: Добавить enum class JlptLevel
                    filterInput { it.controlNewText.isInt() && it.controlNewText.toInt() in 0..5}
                }
            }
        }
        button("Сохранить") {
            enableWhen(itemViewModel.valid)
            action {
                itemViewModel.commit {
                    itemViewModel.onWordSave()
                }
                close()
            }
        }
        paddingAll = 10.0
        vgrow = Priority.ALWAYS
        alignment = Pos.CENTER_LEFT
    }
}