package ru.jr2.edit.presentation.view.kanji.edit

import ru.jr2.edit.Style.Companion.mediumButton
import ru.jr2.edit.Style.Companion.miniButton
import ru.jr2.edit.Style.Companion.utilityFragment
import ru.jr2.edit.domain.model.Kanji
import ru.jr2.edit.presentation.viewmodel.kanji.KanjiEditViewModel
import tornadofx.*

class KanjiEditComponentFragment : Fragment("Редактирование списка компонентов") {
    private val kanjiEditViewModel: KanjiEditViewModel by inject()

    override val root = borderpane {
        paddingAll = 10.0
        center = tableview(kanjiEditViewModel.components) {
            placeholder = label("У моджи нет компонентов")
            column("Моджи", Kanji::kanji)
            column("Интерпретация", Kanji::pInterpretation).remainingWidth()
            smartResize()
            onSelectionChange { kanjiEditViewModel.selectedComponent = it }
        }
        bottom = borderpane {
            right = buttonbar {
                button("-") {
                    addClass(miniButton)
                }.action { kanjiEditViewModel.onComponentRemoveClick() }
                button("⌃") {
                    addClass(miniButton)
                }.action { kanjiEditViewModel.onComponentMoveUpClick() }
                button("˅") {
                    addClass(miniButton)
                }.action { kanjiEditViewModel.onComponentMoveDownClick() }
            }
            left = button("ОК") {
                action { close() }
                addClass(mediumButton)
            }
        }
        addClass(utilityFragment)
    }
}