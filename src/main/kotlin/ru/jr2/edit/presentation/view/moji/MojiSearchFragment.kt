package ru.jr2.edit.presentation.view.moji

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import org.controlsfx.glyphfont.FontAwesome
import ru.jr2.edit.data.db.repository.MojiDbRepository
import ru.jr2.edit.domain.model.Moji
import tornadofx.*
import tornadofx.controlsfx.customTextfield
import tornadofx.controlsfx.toGlyph

class MojiSearchFragment : Fragment("Поиск моджи") {
    private val viewModel: MojiSearchViewModel by inject()
    override val root = borderpane {
        top = customTextfield(right = FontAwesome.Glyph.SEARCH.toGlyph()) {
            textProperty().addListener { _, _, query ->
                viewModel.onSearchQueryChange(query)
            }
        }
        center = tableview(viewModel.mojis) {
            column("Моджи", Moji::valueProp)
            smartResize()
            placeholder = label("Нет моджи по заданному запросу")
            onUserSelect(2) { moji ->
                viewModel.onMojiSelect(moji)
                close()
            }
        }
    }
}

class MojiSearchViewModel(
    private val mojiRepository: MojiDbRepository = MojiDbRepository()
) : ViewModel() {
    val mojis: ObservableList<Moji> = FXCollections.observableArrayList<Moji>()

    fun onSearchQueryChange(query: String) {
        if (!query.isBlank()) {
            mojis.clear()
            mojis.addAll(mojiRepository.getBySearchQuery(query))
        }
    }

    fun onMojiSelect(moji: Moji) {
        fire(MojiSelectedEvent(moji))
    }
}

class MojiSelectedEvent(val moji: Moji) : FXEvent()