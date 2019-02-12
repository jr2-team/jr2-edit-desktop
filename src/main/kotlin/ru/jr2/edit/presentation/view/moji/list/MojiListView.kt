package ru.jr2.edit.presentation.view.moji.list

import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.ButtonBar
import javafx.scene.control.ButtonType
import ru.jr2.edit.Style.Companion.bottomButtonPane
import ru.jr2.edit.domain.model.Moji
import ru.jr2.edit.presentation.viewmodel.moji.MojiListViewModel
import tornadofx.*

class MojiListView : View() {
    private val viewModel: MojiListViewModel by inject()

    private var btnEditMoji: Button by singleAssign()
    private var btnDelete: Button by singleAssign()

    override val root = borderpane {
        center = tableview(viewModel.mojis) {
            column("Значение", Moji::pValue)
            column("Интерпретации", Moji::pInterpretation).remainingWidth()
            column("Кунное чтение", Moji::pKunReading)
            column("Онное чтение", Moji::pOnReading)
            column("Уровень JLPT", Moji::pJlptLevel)
            column("Тип моджи", Moji::pMojiType)
            smartResize()
            onSelectionChange { moji ->
                (moji !is Moji).let {
                    btnEditMoji.isDisable = it
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
                btnEditMoji = button("Редактировать") {
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

    private fun showDeleteMojiWarning() = warning(
        "Удалить моджи",
        "Вы уверены, что хотите удалить ${viewModel.selectedMoji.toString()}?",
        ButtonType.OK, ButtonType.CANCEL,
        title = "Удалить моджи",
        actionFn = {
            when (it.buttonData) {
                ButtonBar.ButtonData.OK_DONE -> viewModel.onDeleteMojiClick()
                else -> this.close()
            }
        }
    )
}