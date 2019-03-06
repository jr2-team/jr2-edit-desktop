package ru.jr2.edit.presentation.view.word

import javafx.geometry.Pos
import javafx.scene.control.Button
import ru.jr2.edit.Style
import ru.jr2.edit.Style.Companion.paginationControl
import ru.jr2.edit.presentation.model.WordModel
import ru.jr2.edit.presentation.viewmodel.word.list.WordListViewModel
import ru.jr2.edit.util.showWarningMsg
import tornadofx.*

class WordListView : View() {
    private val viewModel: WordListViewModel by inject()
    private var btnEditWord: Button by singleAssign()
    private var btnDeleteWord: Button by singleAssign()

    override fun onTabSelected() {
        super.onTabSelected()
        viewModel.loadContent()
    }

    override val root = borderpane {
        top = renderAdditionalFunctionHBox()
        center = renderWordTableView()
        bottom = borderpane {
            right = renderPaginationControlHBox()
            left = renderContentControlButtonBar()
            addClass(Style.bottomBorderPaneStyle)
        }
    }

    private fun renderAdditionalFunctionHBox() = hbox {
        paddingAll = 5
        button("Слова из Edict").action { viewModel.onParseClick() }
        button("Обновить данные").action { viewModel.loadContent() }
    }

    private fun renderWordTableView() = tableview(viewModel.words) {
        column("Слово", WordModel::pWord).weightedWidth(1)
        column("Фуригана", WordModel::pFurigana).weightedWidth(1)
        column("Интерпретация", WordModel::pInterpretation).weightedWidth(3)
        column("Уровень JLPT", WordModel::pJlptLevel).weightedWidth(1)
        smartResize()
        onSelectionChange { word ->
            (word !is WordModel).let {
                btnEditWord.isDisable = it
                btnDeleteWord.isDisable = it
            }
            viewModel.selectedWord = word
        }
        onUserSelect(2) { viewModel.onEditWordClick() }
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

    private fun showDeleteWordWarning() = showWarningMsg(
        "Удалить слово",
        "Вы уверены, что хотите удалить ${viewModel.selectedWord.toString()}?",
        viewModel::onDeleteWordClick
    )
}