package ru.jr2.edit.presentation.word.view

import javafx.geometry.Pos
import ru.jr2.edit.Style.Companion.largeButton
import ru.jr2.edit.presentation.base.view.BaseEditFragment
import ru.jr2.edit.presentation.word.model.WordInterpretationModel
import ru.jr2.edit.presentation.word.model.WordModel
import ru.jr2.edit.presentation.word.viewmodel.edit.WordEditViewModel
import ru.jr2.edit.util.JlptLevel
import tornadofx.*

class WordEditFragment : BaseEditFragment<WordModel, WordEditViewModel>() {
    override val viewModel = WordEditViewModel(paramItemId)

    override val root = borderpane {
        paddingAll = 10
        center = form {
            add(renderWordPropertiesFieldSet())
            add(renderWordInterpretationBorderPane())
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
        field("Уровень JLPT") {
            combobox(viewModel.pJlptLevel, JlptLevel.getNames())
        }
    }

    private fun renderWordInterpretationBorderPane() = borderpane {
        setPrefSize(400.0, 400.0)
        top = label("Интерпритации")
        center = tableview(viewModel.wordInterps) {
            column("Язык", WordInterpretationModel::pLanguage).weightedWidth(1)
            column("Часть речи", WordInterpretationModel::pPos).weightedWidth(1)
            column("Интерпритация", WordInterpretationModel::pInterpretation).weightedWidth(3)
        }
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