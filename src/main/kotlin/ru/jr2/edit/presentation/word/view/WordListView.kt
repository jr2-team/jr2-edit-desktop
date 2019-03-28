package ru.jr2.edit.presentation.word.view

import javafx.geometry.Pos
import javafx.scene.control.Button
import ru.jr2.edit.Style
import ru.jr2.edit.Style.Companion.paginationControl
import ru.jr2.edit.domain.dto.WordDto
import ru.jr2.edit.presentation.word.viewmodel.list.WordListViewModel
import ru.jr2.edit.util.showWarningMsg
import tornadofx.*

class WordListView : View() {
    private val viewModel: WordListViewModel by inject()

    private var btnEditWord: Button by singleAssign()
    private var btnDeleteWord: Button by singleAssign()

    override fun onTabSelected() {
        super.onTabSelected()
        if (viewModel.words.isEmpty()) viewModel.loadContent()
    }

    override val root = borderpane {
        top = renderAdditionalFunctionHBox()
        center = renderWordTableView()
        bottom = borderpane {
            left = renderContentControlButtonBar()
            right = renderPaginationControlHBox()
            addClass(Style.bottomBorderPaneStyle)
        }
    }

    private fun renderAdditionalFunctionHBox() = hbox {
        paddingAll = 5
        button("Обновить данные").action { viewModel.loadContent() }
    }

    private fun renderWordTableView() = tableview(viewModel.words) {
        placeholder = progressindicator { setMaxSize(28.0, 28.0) }
        column("Слово", WordDto::word).weightedWidth(1)
        column("Фуригана", WordDto::furigana).weightedWidth(1)
        column("Интерпретация", WordDto::interps).weightedWidth(5)
        smartResize()
        onSelectionChange { word ->
            (word !is WordDto).let {
                btnEditWord.isDisable = it
                btnDeleteWord.isDisable = it
            }
            viewModel.selectedWord = word
        }
        onUserSelect(2) { viewModel.onEditWordClick() }
    }

    private fun renderContentControlButtonBar() = buttonbar {
        button("Добавить").action { viewModel.onNewWordClick() }
        btnEditWord = button("Редактировать") {
            isDisable = true
            action { viewModel.onEditWordClick() }
        }
        btnDeleteWord = button("Удалить") {
            isDisable = true
            action { showDeleteWordWarning() }
        }
    }

    private fun renderPaginationControlHBox() = hbox {
        button("-") {
            action { viewModel.onChangePageClick(false) }
        }
        textfield(viewModel.pCurrentPage) {
            alignment = Pos.BASELINE_CENTER
            filterInput {
                with(it.controlNewText) {
                    isInt() && toInt() in 1..viewModel.pTotalPageCount.value
                }
            }
        }
        button("+").action { viewModel.onChangePageClick(true) }
        label(viewModel.pTotalPageCount) {
            alignment = Pos.BASELINE_CENTER
        }
        addClass(paginationControl)
    }

    private fun showDeleteWordWarning() = showWarningMsg(
        "Удалить слово",
        "Вы уверены, что хотите удалить ${viewModel.selectedWord.toString()}?",
        viewModel::onDeleteWordClick
    )
}