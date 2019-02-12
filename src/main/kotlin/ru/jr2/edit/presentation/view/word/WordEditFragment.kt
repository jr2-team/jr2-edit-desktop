package ru.jr2.edit.presentation.view.word

import javafx.geometry.Pos
import javafx.scene.layout.Priority
import ru.jr2.edit.Style.Companion.largeButton
import ru.jr2.edit.domain.JlptLevel
import ru.jr2.edit.presentation.view.BaseFragment
import ru.jr2.edit.presentation.viewmodel.word.WordEditViewModel
import tornadofx.*

class WordEditFragment : BaseFragment() {
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
                    textfield(viewModel.pValue).required(message = "Обязательное поле")
                }
                field("Фуригана") {
                    textfield(viewModel.pFurigana)
                }
                field("Основные интерпритации") {
                    textarea(viewModel.pInterpretation) {
                        vgrow = Priority.NEVER
                        required(message = "Обязательное поле")
                    }
                }
                field("Уровень JLPT") {
                    combobox(viewModel.pJlptLevel, JlptLevel.getNames())
                }
            }
        }

        bottom = hbox {
            button("Сохранить") {
                enableWhen(viewModel.valid)
                action {
                    viewModel.commit { viewModel.onSaveClick() }
                    close()
                }
                addClass(largeButton)
            }
            alignment = Pos.BOTTOM_RIGHT
        }
        paddingAll = 10.0
    }
}