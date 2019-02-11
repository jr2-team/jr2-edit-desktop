package ru.jr2.edit.presentation.view.moji

import javafx.geometry.Pos
import ru.jr2.edit.domain.model.Moji
import ru.jr2.edit.presentation.viewmodel.moji.MojiEditViewModel
import tornadofx.*

class MojiEditComponentFragment : Fragment("") {
    private val viewModel: MojiEditViewModel by inject()

    override val root = borderpane {
        center = tableview(viewModel.components) {
            placeholder = label("У моджи нет компонентов")
            column("Моджи", Moji::pValue)
            smartResize()
            onSelectionChange { moji ->
                viewModel.selectedComponent = moji
            }
        }
        bottom = borderpane {
            right = buttonbar {
                button("Убрать") {
                    action { viewModel.onComponentRemoveClick() }
                }
                button("Выше") {
                    action { viewModel.onComponentMoveUpClick() }
                }
                button("Ниже") {
                    action { viewModel.onComponentMoveDownClick() }
                }
                style {
                    alignment = Pos.BASELINE_RIGHT
                }
            }
            left = button("ОК") {
                action { close() }
            }
            paddingTop = 10.0
        }
        paddingAll = 10.0
    }
}