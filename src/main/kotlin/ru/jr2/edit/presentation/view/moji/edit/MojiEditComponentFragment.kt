package ru.jr2.edit.presentation.view.moji.edit

import ru.jr2.edit.Style.Companion.mediumButton
import ru.jr2.edit.Style.Companion.miniButton
import ru.jr2.edit.Style.Companion.utilityFragment
import ru.jr2.edit.domain.model.Moji
import ru.jr2.edit.presentation.viewmodel.moji.MojiEditViewModel
import tornadofx.*

class MojiEditComponentFragment : Fragment("Редактирование списка компонентов") {
    private val viewModel: MojiEditViewModel by inject()

    override val root = borderpane {
        center = tableview(viewModel.components) {
            placeholder = label("У моджи нет компонентов")
            column("Моджи", Moji::pValue)
            column("Вид", Moji::pMojiType)
            column("Интерпретация", Moji::pInterpretation).remainingWidth()
            smartResize()
            onSelectionChange { viewModel.selectedComponent = it }
        }

        bottom = borderpane {
            right = buttonbar {
                button("-") {
                    action { viewModel.onComponentRemoveClick() }
                    addClass(miniButton)
                }
                button("⌃") {
                    action { viewModel.onComponentMoveUpClick() }
                    addClass(miniButton)
                }
                button("˅") {
                    addClass(miniButton)
                    action { viewModel.onComponentMoveDownClick() }
                }
            }

            left = button("ОК") {
                action { close() }
                addClass(mediumButton)
            }

            paddingAll = 10.0
        }

        addClass(utilityFragment)
    }
}