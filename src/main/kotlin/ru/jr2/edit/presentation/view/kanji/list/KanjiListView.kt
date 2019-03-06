package ru.jr2.edit.presentation.view.kanji.list

import javafx.geometry.Pos
import javafx.scene.control.Button
import ru.jr2.edit.Style.Companion.bottomBorderPaneStyle
import ru.jr2.edit.domain.dto.KanjiDto
import ru.jr2.edit.presentation.viewmodel.kanji.list.KanjiListViewModel
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
        bottom = renderContentControlBorderPane()
    }

    private fun renderAdditionalFunctionHBox() = hbox {
        paddingAll = 5
        button("Канджи из Edict").action { viewModel.onParseClick() }
        button("KanjiVG").action { }
        button("Обновить данные").action { viewModel.loadContent() }
    }

    private fun renderKanjiTableView() = tableview(viewModel.kanjis) {
        columnResizePolicy = SmartResize.POLICY
        column(String(), KanjiDto::kanji) {
            style {
                alignment = Pos.BASELINE_CENTER
                fontSize = 18.px
            }
        }.contentWidth()
        column("Интерпретации", KanjiDto::interpretation).weightedWidth(3)
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

    private fun renderContentControlBorderPane() = borderpane {
        right = button("Фильтровать")
        left = buttonbar {
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
        }
        addClass(bottomBorderPaneStyle)
    }

    private fun showDeleteMojiWarning() = showWarningMsg(
        "Удалить канджи",
        "Вы уверены, что хотите удалить ...?",
        viewModel::onDeleteKanjiClick
    )
}