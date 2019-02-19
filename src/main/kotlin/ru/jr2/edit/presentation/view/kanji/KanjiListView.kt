package ru.jr2.edit.presentation.view.kanji

import javafx.geometry.Pos
import javafx.scene.control.Button
import ru.jr2.edit.Style.Companion.bottomButtonPane
import ru.jr2.edit.Style.Companion.mediumButton
import ru.jr2.edit.domain.model.Kanji
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
            column(String(), Kanji::kanji) {
                style {
                    alignment = Pos.BASELINE_CENTER
                    fontSize = 18.px
                }
            }.contentWidth()
            column("Интерпретации", Kanji::pInterpretation)
            column("Кунное чтение", Kanji::pKunReading)
            column("Онное чтение", Kanji::pOnReading)
            column("Уровень JLPT", Kanji::pJlptLevel)
            columnResizePolicy = SmartResize.POLICY
            onSelectionChange { moji ->
                (moji !is Kanji).let {
                    btnEdit.isDisable = it
                    btnDelete.isDisable = it
                }
                moji?.let {
                    viewModel.selectedKanji = it
                    viewModel.onKanjiSelect(it)
                }
            }
            onUserSelect(2) {
                viewModel.onEditKanjiClick()
            }
        }
        right = listview(viewModel.components) {
            placeholder = label("Нет компонентов")
            cellFormat {
                graphic = vbox {
                    alignment = Pos.CENTER
                    label(it.toString())
                }
                lineSpacing = 0.5
            }
            onUserSelect(2) {
                viewModel.selectedKanji = it
                viewModel.onEditKanjiClick()
            }
            this.minWidth = 120.0
            this.maxWidth = 120.0
        }
        bottom = borderpane {
            right = button("Фильтровать")

            left = buttonbar {
                button("Добавить") {
                    action { viewModel.onNewKanjiClick() }
                }
                btnEdit = button("Редактировать") {
                    action { viewModel.onEditKanjiClick() }
                    isDisable = true
                }
                btnDelete = button("Удалить") {
                    action { showDeleteMojiWarning() }
                    isDisable = true
                }
            }
            addClass(bottomButtonPane)
        }
    }

    private fun showDeleteMojiWarning() = showWarningMsg(
        "Удалить моджи",
        "Вы уверены, что хотите удалить ${viewModel.selectedKanji.toString()}?",
        viewModel::onDeleteKanjiClick
    )
}