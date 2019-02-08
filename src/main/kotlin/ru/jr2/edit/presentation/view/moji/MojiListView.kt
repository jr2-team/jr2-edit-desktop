package ru.jr2.edit.presentation.view.moji

import javafx.scene.control.Button
import ru.jr2.edit.domain.model.Moji
import ru.jr2.edit.presentation.viewmodel.moji.MojiListViewModel
import tornadofx.*

class MojiListView : View() {
    private val viewModel: MojiListViewModel by inject()

    private var btnEditMoji: Button by singleAssign()

    override val root = borderpane {
        center = tableview(viewModel.mojis) {
            column("ID", Moji::id)
            column("Значение", Moji::value)
            column("Интерпретации", Moji::basicInterpretation)
                .remainingWidth()
            column("Кунное чтение", Moji::kunReading)
            column("Онное чтение", Moji::onReading)
            column("Уровень JLPT", Moji::jlptLevel)
            smartResize()
            onSelectionChange { moji ->
                btnEditMoji.isDisable = moji == null
                moji?.run {
                    viewModel.selectedMoji = this
                    viewModel.onMojiSelect(this)
                }
            }
            onUserSelect(2) {
                viewModel.onShowEditMojiFragment()
            }
        }
        right = listview(viewModel.components) {
            cellFormat {
                graphic = vbox {
                    label(it.id.toString())
                    label(it.value)
                }
                lineSpacing = 0.5
            }
            onUserSelect(2) {
                viewModel.selectedMoji = it
                viewModel.onShowEditMojiFragment()
            }
        }
        bottom = borderpane {
            right = hbox {
                togglegroup {
                    togglebutton("Канджи") {
                        setMinSize(120.0, 28.0)
                        action { viewModel.onFilterMojiType(1) }
                    }
                    togglebutton("Радикалы") {
                        setMinSize(120.0, 28.0)
                        action { viewModel.onFilterMojiType(0) }
                    }
                    togglebutton("Все") {
                        setMinSize(120.0, 28.0)
                        action { viewModel.onFilterMojiType() }
                        isSelected = true
                    }
                }
            }
            left = buttonbar {
                button("Добавить") {
                    setMinSize(120.0, 28.0)
                    action { viewModel.onShowNewMojiFragment() }
                }
                btnEditMoji = button("Редактировать") {
                    setMinSize(120.0, 28.0)
                    action { viewModel.onShowEditMojiFragment() }
                    isDisable = true
                }
            }
            paddingAll = 10.0
        }
    }
}