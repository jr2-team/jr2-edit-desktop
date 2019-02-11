package ru.jr2.edit.presentation.view.moji

import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.ButtonBar
import javafx.scene.control.ButtonType
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
            right = button("Фильтровать") {
                setMinSize(120.0, 28.0)
            }
            left = buttonbar {
                button("Добавить") {
                    setMinSize(120.0, 28.0)
                    action { viewModel.onNewMojiClick() }
                }
                btnEditMoji = button("Редактировать") {
                    setMinSize(120.0, 28.0)
                    action { viewModel.onEditMojiClick() }
                    isDisable = true
                }
                btnDelete = button("Удалить") {
                    setMinSize(120.0, 28.0)
                    action {
                        showDeleteMojiWarning()
                    }
                    isDisable = true
                }
            }
            paddingAll = 10.0
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