package ru.jr2.edit.presentation.kanji.view.edit

import org.controlsfx.glyphfont.FontAwesome
import ru.jr2.edit.Style
import ru.jr2.edit.Style.Companion.utilityFragment
import ru.jr2.edit.presentation.kanji.model.KanjiModel
import ru.jr2.edit.presentation.kanji.viewmodel.edit.KanjiEditViewModel
import ru.jr2.edit.presentation.kanji.viewmodel.edit.KanjiSearchViewModel
import tornadofx.*
import tornadofx.controlsfx.*

class KanjiEditSearchFragment : Fragment("Поиск моджи") {
    private val viewModel: KanjiEditViewModel by inject()
    private val searchViewModel: KanjiSearchViewModel by inject()

    override val root = borderpane {
        top = renderSearchTextField()
        center = renderKanjiTableView()
        bottom = renderAddOKButtonsBorderPane()
        addClass(utilityFragment)
    }

    private fun renderSearchTextField() = customTextfield(
        right = FontAwesome.Glyph.SEARCH.toGlyph()
    ) {
        textProperty().addListener { _, _, query ->
            searchViewModel.onSearchQueryChanged(query)
        }
    }

    private fun renderKanjiTableView() = tableview(searchViewModel.kanjis) {
        placeholder = label("Нет канджи по заданному запросу")
        column("Канджи", KanjiModel::pKanji)
        column("Интерпретация", KanjiModel::pInterp).remainingWidth()
        smartResize()
        onSelectionChange { moji ->
            viewModel.selectedComponent = moji
        }
        onUserSelect(2) {
            viewModel.onComponentAddClick()
        }
    }

    private fun renderAddOKButtonsBorderPane() = borderpane {
        paddingAll = 10
        right = button("Добавить") {
            action { viewModel.onComponentAddClick() }
            addClass(Style.mediumButton)
        }

        left = button("ОК") {
            action { close() }
            addClass(Style.mediumButton)
        }
    }
}