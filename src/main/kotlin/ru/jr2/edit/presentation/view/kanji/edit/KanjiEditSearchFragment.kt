package ru.jr2.edit.presentation.view.kanji.edit

import org.controlsfx.glyphfont.FontAwesome
import ru.jr2.edit.Style
import ru.jr2.edit.Style.Companion.utilityFragment
import ru.jr2.edit.domain.model.Kanji
import ru.jr2.edit.presentation.viewmodel.kanji.KanjiEditViewModel
import ru.jr2.edit.presentation.viewmodel.kanji.KanjiSearchViewModel
import tornadofx.*
import tornadofx.controlsfx.customTextfield
import tornadofx.controlsfx.toGlyph

class KanjiEditSearchFragment : Fragment("Поиск моджи") {
    private val viewModel: KanjiEditViewModel by inject()
    private val searchViewModel: KanjiSearchViewModel by inject()

    override val root = borderpane {
        top = customTextfield(right = FontAwesome.Glyph.SEARCH.toGlyph()) {
            textProperty().addListener { _, _, query ->
                searchViewModel.onSearchQueryChanged(query)
            }
        }
        center = tableview(searchViewModel.kanjis) {
            placeholder = label("Нет канджи по заданному запросу")
            column("Канджи", Kanji::pKanji)
            column("Интерпретация", Kanji::pInterpretation).remainingWidth()
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