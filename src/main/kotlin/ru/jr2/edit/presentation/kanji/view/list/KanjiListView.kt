package ru.jr2.edit.presentation.kanji.view.list

import javafx.geometry.Pos
import javafx.scene.control.Button
import ru.jr2.edit.Style
import ru.jr2.edit.Style.Companion.bottomBorderPaneStyle
import ru.jr2.edit.domain.dto.KanjiDto
import ru.jr2.edit.presentation.kanji.viewmodel.list.KanjiListViewModel
import ru.jr2.edit.util.showWarningMsg
import tornadofx.*

class KanjiListView : View() {
    private val viewModel: KanjiListViewModel by inject()

    private var btnEdit: Button by singleAssign()
    private var btnDelete: Button by singleAssign()

    override fun onTabSelected() {
        super.onTabSelected()
        viewModel.loadContent()
    }

    override val root = borderpane {
        top = renderAdditionalFunctionHBox()
        center = renderKanjiTableView()
        right = renderKanjiComponentListView()
        bottom = borderpane {
            left = renderContentControlButtonBar()
            right = renderPaginationControlHBox()
            addClass(bottomBorderPaneStyle)
        }
    }

    private fun renderAdditionalFunctionHBox() = hbox {
        paddingAll = 5
        button("KanjiVG").action { }
        button("Обновить данные").action { viewModel.loadContent() }
    }

    private fun renderKanjiTableView() = tableview(viewModel.kanjis) {
        placeholder = progressindicator { setMaxSize(28.0, 28.0) }
        columnResizePolicy = SmartResize.POLICY
        column(String(), KanjiDto::kanji) {
            style {
                alignment = Pos.BASELINE_CENTER
                fontSize = 18.px
            }
        }.contentWidth()
        column("Интерпретации", KanjiDto::interp).weightedWidth(3)
        column("Онное чтение", KanjiDto::onReadings).weightedWidth(1)
        column("Кунное чтение", KanjiDto::kunReadings).weightedWidth(1)
        column("Уровень JLPT", KanjiDto::jlptLevel).weightedWidth(1)
        onSelectionChange { kanji ->
            (kanji !is KanjiDto).let {
                btnEdit.isDisable = it
                btnDelete.isDisable = it
            }
            kanji?.let { viewModel.onKanjiSelectChange(it.id, needToLoadComponents = true) }
        }
        onUserSelect(2) { viewModel.onEditKanjiClick() }
    }

    private fun renderKanjiComponentListView() = listview(viewModel.components) {
        placeholder = label("Нет компонентов")
        minWidth = 120.0
        maxWidth = 120.0
        cellFormat {
            graphic = vbox {
                alignment = Pos.CENTER
                label(it.toString())
            }
            lineSpacing = 0.5
        }
        onUserSelect(2) {
            viewModel.onKanjiSelectChange(it.id)
            viewModel.onEditKanjiClick()
        }
    }

    private fun renderContentControlButtonBar() = buttonbar {
        button("Добавить") {
            action { viewModel.onNewKanjiClick() }
        }
        btnEdit = button("Редактировать") {
            isDisable = true
            action { viewModel.onEditKanjiClick() }
        }
        btnDelete = button("Удалить") {
            isDisable = true
            action { showDeleteMojiWarning() }
        }
        button("Фильтровать")
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
        addClass(Style.paginationControl)
    }

    private fun showDeleteMojiWarning() = showWarningMsg(
        "Удалить канджи",
        "Вы уверены, что хотите удалить ...?",
        viewModel::onDeleteKanjiClick
    )
}