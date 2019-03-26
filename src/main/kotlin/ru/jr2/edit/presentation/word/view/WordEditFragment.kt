package ru.jr2.edit.presentation.word.view

import javafx.geometry.Pos
import javafx.scene.layout.Priority
import ru.jr2.edit.Style.Companion.largeButton
import ru.jr2.edit.domain.misc.JlptLevel
import ru.jr2.edit.presentation.word.model.WordModel
import ru.jr2.edit.presentation.base.view.BaseEditFragment
import ru.jr2.edit.presentation.word.viewmodel.edit.WordEditViewModel
import tornadofx.*

class WordEditFragment : BaseEditFragment<WordModel, WordEditViewModel>() {
    override val viewModel = WordEditViewModel(paramItemId)

    override val root = borderpane {
        paddingAll = 10
        center = form {
            add(renderWordPropertiesFieldSet())
        }
        bottom = renderSaveHBox()
    }

    private fun renderWordPropertiesFieldSet() = fieldset {
        field("Слово") {
            textfield(viewModel.pWord).required(message = requiredMsg)
        }
        field("Фуригана") {
            textfield(viewModel.pFurigana)
        }
        field("Основные интерпритации") {
            textarea(viewModel.pInterpretation) {
                vgrow = Priority.NEVER
                required(message = requiredMsg)
            }
        }
        field("Уровень JLPT") {
            combobox(viewModel.pJlptLevel, JlptLevel.getNames())
        }
    }

    private fun renderWordInterpretationBorderPane() = borderpane {
        top = label("Интеритации")
        // center = tableview() {  }
        bottom = hbox { }
    }

    private fun renderSaveHBox() = hbox {
        button("Сохранить") {
            enableWhen(viewModel.valid)
            addClass(largeButton)
        }.action {
            viewModel.onSaveClick()
            close()
        }
        alignment = Pos.BOTTOM_RIGHT
    }
}