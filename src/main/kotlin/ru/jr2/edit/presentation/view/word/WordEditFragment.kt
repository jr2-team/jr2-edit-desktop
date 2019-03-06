package ru.jr2.edit.presentation.view.word

import javafx.geometry.Pos
import javafx.scene.layout.Priority
import ru.jr2.edit.Style.Companion.largeButton
import ru.jr2.edit.domain.misc.JlptLevel
import ru.jr2.edit.domain.model.WordModel
import ru.jr2.edit.presentation.view.BaseEditFragment
import ru.jr2.edit.presentation.viewmodel.word.WordEditViewModel
import tornadofx.*

class WordEditFragment : BaseEditFragment<WordModel, WordEditViewModel>() {
    override val viewModel = WordEditViewModel(paramItemId)

    override val root = borderpane {
        center = form {
            fieldset {
                field("Слово") {
                    textfield(viewModel.pWord).required(message = requiredMsg)
                }
                field("Фуригана") {
                    textfield(viewModel.pFurigana)
                }
                field("Основные интерпритации") {
                    textarea(viewModel.pInterpretation) {
                        vgrow = Priority.NEVER
                    }.required(message = requiredMsg)
                }
                field("Уровень JLPT") {
                    combobox(viewModel.pJlptLevel, JlptLevel.getNames())
                }
            }
        }
        bottom = hbox {
            button("Сохранить") {
                enableWhen(viewModel.valid)
                addClass(largeButton)
            }.action {
                viewModel.onSaveClick()
                close()
            }
            alignment = Pos.BOTTOM_RIGHT
        }
        paddingAll = 10.0
    }
}