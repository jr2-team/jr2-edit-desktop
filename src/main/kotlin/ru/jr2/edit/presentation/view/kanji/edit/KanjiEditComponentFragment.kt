package ru.jr2.edit.presentation.view.kanji.edit

import ru.jr2.edit.Style.Companion.mediumButton
import ru.jr2.edit.Style.Companion.miniButton
import ru.jr2.edit.Style.Companion.utilityFragment
import ru.jr2.edit.domain.model.Kanji
import ru.jr2.edit.presentation.viewmodel.kanji.KanjiEditViewModel
import tornadofx.*

class KanjiEditComponentFragment : Fragment("Редактирование списка компонентов") {
    private val viewModel: KanjiEditViewModel by inject()

    override val root = borderpane {
        center = tableview(viewModel.components) {
            placeholder = label("У моджи нет компонентов")
            column("Моджи", Kanji::kanji)
            column("Интерпретация", Kanji::pInterpretation).remainingWidth()
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