package ru.jr2.edit.presentation.view.sentence

import javafx.geometry.Pos
import javafx.scene.layout.Priority
import ru.jr2.edit.Style
import ru.jr2.edit.presentation.view.BaseEditFragment
import ru.jr2.edit.presentation.viewmodel.sentence.SentenceEditViewModel
import tornadofx.*

class SentenceEditFragment : BaseEditFragment("предложение") {
    private val viewModel: SentenceEditViewModel = SentenceEditViewModel(baseModelId)

    override val root = borderpane {
        center = form {
            fieldset {
                field("Предложение") {
                    textarea(viewModel.pValue) {
                        vgrow = Priority.NEVER
                        required()
                    }
                }
                field("Фуригана") {
                    textarea(viewModel.pFurigana) {
                        vgrow = Priority.NEVER
                    }
                }
                field("Интерпретация") {
                    textarea(viewModel.pInterpretation) {
                        vgrow = Priority.NEVER
                    }
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
                addClass(Style.largeButton)
            }
            alignment = Pos.BOTTOM_RIGHT
        }
        paddingAll = 10.0
    }
}