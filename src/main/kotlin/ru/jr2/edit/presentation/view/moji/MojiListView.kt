package ru.jr2.edit.presentation.view.moji

import javafx.geometry.Pos
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
                    viewModel.onMojiSelect(this)
                }
            }
            onUserSelect(2) {
                viewModel.onShowEditMojiFragment()
            }
        }
        right = borderpane {
            top = label("Составные канджи")
            center = listview(viewModel.components) {
                cellFormat {
                    graphic = cache {
                        form {
                            fieldset {
                                field { label(it.id.toString()) }
                                field { label(it.value) }
                            }
                            separator()
                        }
                    }
                }
                onUserSelect(2) {
                    information(it.value)
                }
            }
            bottom = label("Двойной клик для редактирования")
        }

        bottom = hbox(spacing = 10) {
            button("Добавить") {
                setMinSize(120.0, 28.0)
                action { viewModel.onShowNewMojiFragment() }
            }
            btnEditMoji = button("Редактировать") {
                setMinSize(120.0, 28.0)
                action { viewModel.onShowEditMojiFragment() }
                isDisable = true
            }
            paddingAll = 10.0
            alignment = Pos.BOTTOM_RIGHT
        }
    }
}