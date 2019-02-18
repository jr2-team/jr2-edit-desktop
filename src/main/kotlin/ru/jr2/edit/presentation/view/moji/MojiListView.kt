package ru.jr2.edit.presentation.view.moji

import javafx.geometry.Pos
import javafx.scene.control.Button
import ru.jr2.edit.Style.Companion.bottomButtonPane
import ru.jr2.edit.Style.Companion.mediumButton
import ru.jr2.edit.domain.model.Moji
import ru.jr2.edit.presentation.viewmodel.moji.MojiListViewModel
import ru.jr2.edit.util.showWarningMsg
import tornadofx.*

class MojiListView : View() {
    private val viewModel: MojiListViewModel by inject()

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
        center = tableview(viewModel.mojis) {
            column(String(), Moji::pMoji) {
                style {
                    alignment = Pos.BASELINE_CENTER
                    fontSize = 18.px
                }
            }.contentWidth()
            column("Интерпретации", Moji::pInterpretation)
            column("Кунное чтение", Moji::pKunReading)
            column("Онное чтение", Moji::pOnReading)
            column("Уровень JLPT", Moji::pJlptLevel)
            column("Тип моджи", Moji::pMojiType)
            columnResizePolicy = SmartResize.POLICY
            onSelectionChange { moji ->
                (moji !is Moji).let {
                    btnEdit.isDisable = it
                    btnDelete.isDisable = it
                }
                moji?.let {
                    viewModel.selectedMoji = it
                    viewModel.onMojiSelect(it)
                }
            }
            onUserSelect(2) {
                viewModel.onEditMojiClick()
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
                viewModel.selectedMoji = it
                viewModel.onEditMojiClick()
            }
            this.minWidth = 120.0
            this.maxWidth = 120.0
        }
        bottom = borderpane {
            right = button("Фильтровать")

            left = buttonbar {
                button("Добавить") {
                    action { viewModel.onNewMojiClick() }
                }
                btnEdit = button("Редактировать") {
                    action { viewModel.onEditMojiClick() }
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
        "Вы уверены, что хотите удалить ${viewModel.selectedMoji.toString()}?",
        viewModel::onDeleteMojiClick
    )
}