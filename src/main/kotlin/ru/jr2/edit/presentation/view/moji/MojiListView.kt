package ru.jr2.edit.presentation.view.moji

import ru.jr2.edit.domain.model.Moji
import ru.jr2.edit.presentation.viewmodel.moji.MojiListViewModel
import tornadofx.*

class MojiListView : View() {
    val viewModel: MojiListViewModel by inject()

    override val root = borderpane {
        center = tableview(viewModel.mojis) {
            column("ID", Moji::idProp)
            column("Значение", Moji::valueProp)
            column("Интерпретации", Moji::basicInterpretationProp)
            smartResize()
            onSelectionChange {
                it?.run {
                    viewModel.onKanjiSelected(this)
                }
            }
        }
        right = listview(viewModel.components) {
            cellFormat {
                graphic = cache {
                    form { fieldset {
                            field { label(it.id.toString()) }
                            field { label(it.value) }
                        }
                        separator()
                    }
                }
            }
        }

    }
}