package ru.jr2.edit.presentation.view.word

import javafx.geometry.Pos
import javafx.scene.layout.Priority
import ru.jr2.edit.presentation.viewmodel.word.edit.WordEditViewModel
import tornadofx.*

class WordEditFragment : Fragment("Добавить слово") {
    private var viewModel: WordEditViewModel

    val wordIdParam: Int by param(0)

    init {
        viewModel = WordEditViewModel(wordIdParam)
        if (wordIdParam == 0) {
            this.title = "Добавить слово"
        } else {
            this.title = "Редактировать слово"
        }
    }

    override val root = form {
        fieldset("New word") {
            field("Value") {
                textfield(viewModel.observableWord.valueProp)
            }
            field("Furigana") {
                textfield(viewModel.observableWord.furiganaProp)
            }
            field("Basic Interpretation") {
                textfield(viewModel.observableWord.basicInterpretationProp)
            }
            field("JLPT level") {
                textfield(viewModel.observableWord.jlptLevelProp)
            }
        }
        button("Сохранить") {
            //enableWhen(viewModel.observableValidationState)
            action {
                viewModel.onWordSave()
                close()
            }
        }
        paddingAll = 10.0
        vgrow = Priority.ALWAYS
        alignment = Pos.CENTER_LEFT
    }
}