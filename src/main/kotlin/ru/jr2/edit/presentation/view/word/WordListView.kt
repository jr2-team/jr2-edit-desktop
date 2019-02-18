package ru.jr2.edit.presentation.view.word

import javafx.geometry.Pos
import javafx.scene.control.Button
import ru.jr2.edit.Style
import ru.jr2.edit.Style.Companion.paginationControl
import ru.jr2.edit.domain.model.Word
import ru.jr2.edit.presentation.viewmodel.word.WordListViewModel
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
        top = hbox {
            button("Edict") {
                addClass(Style.mediumButton)
            }.action { viewModel.onParseClick() }
            button("Обновить данные") {
                addClass(Style.mediumButton)
            }.action { viewModel.loadContent() }
            paddingAll = 5.0
        }
        center = tableview(viewModel.words) {
            column("Слово", Word::pWord)
            column("Фуригана", Word::pFurigana)
            column("Интерпретация", Word::pInterpretation).remainingWidth()
            column("Уровень JLPT", Word::pJlptLevel)
            smartResize()
            onSelectionChange { word ->
                (word !is Word).let {
                    btnEditWord.isDisable = it
                    btnDeleteWord.isDisable = it
                }
                viewModel.selectedWord = word
            }
            onUserSelect(2) { viewModel.onEditWordClick() }
        }
        bottom = borderpane {
            right = hbox {
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
            left = buttonbar {
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
            addClass(Style.bottomButtonPane)
        }
    }

    private fun showDeleteWordWarning() = showWarningMsg(
        "Удалить слово",
        "Вы уверены, что хотите удалить ${viewModel.selectedWord.toString()}?",
        viewModel::onDeleteWordClick
    )
}