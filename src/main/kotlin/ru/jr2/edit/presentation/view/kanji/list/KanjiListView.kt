package ru.jr2.edit.presentation.view.kanji.list

import javafx.geometry.Pos
import javafx.scene.control.Button
import ru.jr2.edit.Style.Companion.bottomButtonPane
import ru.jr2.edit.Style.Companion.mediumButton
import ru.jr2.edit.domain.dto.KanjiDto
import ru.jr2.edit.presentation.viewmodel.kanji.KanjiListViewModel
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
        top = hbox {
            button("Edict") {
                addClass(mediumButton)
            }.action { viewModel.onParseClick() }
            button("KanjiVG") {
                addClass(mediumButton)
            }.action { }
            button("Обновить данные") {
                addClass(mediumButton)
            }.action { viewModel.loadContent() }
            paddingAll = 5.0
        }
        center = tableview(viewModel.kanjis) {
            columnResizePolicy = SmartResize.POLICY
            column(String(), KanjiDto::kanji) {
                style {
                    alignment = Pos.BASELINE_CENTER
                    fontSize = 18.px
                }
            }.contentWidth()
            column("Интерпретации", KanjiDto::interpretation)
            column("Онное чтение", KanjiDto::onReadings)
            column("Кунное чтение", KanjiDto::kunReadings)
            column("Уровень JLPT", KanjiDto::jlptLevel)
            onSelectionChange { kanji ->
                (kanji !is KanjiDto).let {
                    btnEdit.isDisable = it
                    btnDelete.isDisable = it
                }
                kanji?.let {
                    viewModel.onKanjiSelect(it.id, true)
                }
            }
            onUserSelect(2) { viewModel.onEditKanjiClick() }
        }
        right = listview(viewModel.components) {
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
                viewModel.onKanjiSelect(it.id)
                viewModel.onEditKanjiClick()
            }
        }
        bottom = borderpane {
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
            addClass(bottomButtonPane)
        }
    }

    private fun showDeleteMojiWarning() = showWarningMsg(
        "Удалить моджи",
        "Вы уверены, что хотите удалить ...?",
        viewModel::onDeleteKanjiClick
    )
}