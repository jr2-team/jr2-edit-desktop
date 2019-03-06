package ru.jr2.edit.presentation.view.kanji.edit

import ru.jr2.edit.Style
import ru.jr2.edit.Style.Companion.mediumButton
import ru.jr2.edit.Style.Companion.utilityFragment
import ru.jr2.edit.presentation.model.KanjiModel
import ru.jr2.edit.presentation.viewmodel.kanji.edit.KanjiEditViewModel
import tornadofx.*

class KanjiEditComponentFragment : Fragment("Редактирование списка компонентов") {
    private val kanjiEditViewModel: KanjiEditViewModel by inject()

    override val root = borderpane {
        paddingAll = 10.0
        center = renderKanjiComponentTableView()
        bottom = borderpane {
            right = renderKanjiComponentContentControlButtonBar()
            left = renderOKButton()
        }
        addClass(utilityFragment)
    }

    private fun renderKanjiComponentTableView() = tableview(kanjiEditViewModel.kanjiComponents) {
        placeholder = label("У канджи нет компонентов")
        column("Канджи", KanjiModel::kanji)
        column("Интерпретация", KanjiModel::pInterpretation).remainingWidth()
        smartResize()
        onSelectionChange { kanjiEditViewModel.selectedComponent = it }
    }

    private fun renderKanjiComponentContentControlButtonBar() = buttonbar {
        button("-") {
            addClass(Style.miniButton)
        }.action { kanjiEditViewModel.onComponentRemoveClick() }
        button("⌃") {
            addClass(Style.miniButton)
        }.action { kanjiEditViewModel.onComponentMoveUpClick() }
        button("˅") {
            addClass(Style.miniButton)
        }.action { kanjiEditViewModel.onComponentMoveDownClick() }
    }

    private fun renderOKButton() = button("ОК") {
        action { close() }
        addClass(mediumButton)
    }
}