package ru.jr2.edit.presentation.view.moji

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import ru.jr2.edit.data.db.repository.MojiDbRepository
import ru.jr2.edit.domain.model.Moji
import tornadofx.Fragment
import tornadofx.borderpane
import tornadofx.listview

class MojiEditComponentFragment : Fragment() {
    private val viewModel: MojiComponentViewModel

    val mojiIds: List<Int> by param(emptyList())

    init {
        viewModel = MojiComponentViewModel(mojiIds)
    }

    override val root = borderpane {
        center = listview(viewModel.observableMojiComponents) {

        }
    }
}

class MojiComponentViewModel(
    mojiIds: List<Int>,
    private val mojiRepository: MojiDbRepository = MojiDbRepository()
) {
    val observableMojiComponents: ObservableList<Moji> =
        FXCollections.observableArrayList<Moji>()

    init {
        observableMojiComponents.clear()
        observableMojiComponents.addAll(
            mojiIds.map {
                mojiRepository.getById(it)
            }
        )
    }
}