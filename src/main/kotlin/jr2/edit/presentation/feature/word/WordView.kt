package jr2.edit.presentation.feature.word

import javafx.geometry.Pos
import javafx.scene.layout.Priority
import tornadofx.*

class WordView : View() {
    val viewModel: WordViewModel by inject()

    override val root = form {
        fieldset("New word") {
            field("Value") {
                textfield(viewModel.sPropValue)
            }
            field("Furigana") {
                textfield(viewModel.sPropFurigana)
            }
            field("pBasic Interpretation") {
                textfield(viewModel.sPropBasicInterpretation)
            }
            field("JLPT level") {
                textfield(viewModel.iPropJlptLevel)
            }
        }
        button("Write a new word") {
            action {
                viewModel.onWordSave()
            }
        }

        paddingAll = 10.0
        vgrow = Priority.ALWAYS
        alignment = Pos.CENTER_LEFT
    }
}