package ru.jr2.edit.presentation.view.moji.edit

import org.controlsfx.glyphfont.FontAwesome
import ru.jr2.edit.Style
import ru.jr2.edit.Style.Companion.utilityFragment
import ru.jr2.edit.domain.model.Moji
import ru.jr2.edit.presentation.viewmodel.moji.MojiEditViewModel
import ru.jr2.edit.presentation.viewmodel.moji.MojiSearchViewModel
import tornadofx.*
import tornadofx.controlsfx.customTextfield
import tornadofx.controlsfx.toGlyph

class MojiEditSearchFragment : Fragment("Поиск моджи") {
    private val viewModel: MojiEditViewModel by inject()
    private val searchViewModel: MojiSearchViewModel by inject()

    override val root = borderpane {
        top = customTextfield(right = FontAwesome.Glyph.SEARCH.toGlyph()) {
            textProperty().addListener { _, _, query ->
                searchViewModel.onSearchQueryChanged(query)
            }
        }

        center = tableview(searchViewModel.mojis) {
            placeholder = label("Нет моджи по заданному запросу")
            column("Моджи", Moji::pValue)
            column("Вид", Moji::pMojiType)
            column("Интерпретация", Moji::pInterpretation).remainingWidth()
            smartResize()
            onSelectionChange { moji ->
                viewModel.selectedComponent = moji
            }
            onUserSelect(2) {
                viewModel.onComponentAddClick()
            }
        }

        bottom = borderpane {
            right = button("Добавить") {
                action { viewModel.onComponentAddClick() }
                addClass(Style.mediumButton)
            }

            left = button("ОК") {
                action { close() }
                addClass(Style.mediumButton)
            }

            paddingAll = 10.0
        }

        addClass(utilityFragment)
    }
}