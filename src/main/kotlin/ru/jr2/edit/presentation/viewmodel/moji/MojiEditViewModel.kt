package ru.jr2.edit.presentation.viewmodel.moji

import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.stage.StageStyle
import ru.jr2.edit.data.db.repository.MojiDbRepository
import ru.jr2.edit.domain.model.Moji
import ru.jr2.edit.presentation.view.moji.edit.MojiEditComponentFragment
import ru.jr2.edit.presentation.view.moji.edit.MojiEditSearchFragment
import ru.jr2.edit.presentation.viewmodel.EditMode
import tornadofx.*

class MojiEditViewModel(
    mojiId: Int,
    private val mode: EditMode = if (mojiId == 0) EditMode.CREATE else EditMode.UPDATE,
    private val mojiRepository: MojiDbRepository = MojiDbRepository()
) : ItemViewModel<Moji>() {
    val pValue = bind(Moji::pValue)
    val pStrokeCount = bind(Moji::pStrokeCount)
    val pOnReading = bind(Moji::pOnReading)
    val pKunReading = bind(Moji::pKunReading)
    val pInterpretation = bind(Moji::pInterpretation)
    val pJlptLevel = bind(Moji::pJlptLevel)
    val pMojiType = bind(Moji::pMojiType)
    val pComponents = SimpleStringProperty(String())

    val components: ObservableList<Moji> = FXCollections.observableArrayList<Moji>()
    var selectedComponent: Moji? = null

    init {
        components.onChange {
            pComponents.value = components.joinToString { it.value }
        }
        item = when (mode) {
            EditMode.UPDATE -> {
                components.addAll(mojiRepository.getComponentsOfMoji(mojiId))
                mojiRepository.getById(mojiId)
            }
            EditMode.CREATE -> Moji()
        }
    }

    fun onMojiSearchClick() {
        find<MojiEditSearchFragment>(Scope(this))
            .openModal(StageStyle.UTILITY, resizable = false)
    }

    fun onEditComponentClick() {
        find<MojiEditComponentFragment>(Scope(this))
            .openModal(StageStyle.UTILITY, resizable = false)
    }

    fun onSaveClick() {
        mojiRepository.insertUpdateMojiComponent(item, components)
        fire(MojiSavedEvent(true))
    }

    fun onComponentRemoveClick() = selectedComponent?.let {
        components.remove(it)
    }

    fun onComponentAddClick() = selectedComponent?.let {
        if (!components.contains(it)) components.add(it)
    }

    fun onComponentMoveUpClick() = selectedComponent?.let {
        val selectedIdx = components.indexOf(it)
        if (selectedIdx > 0) {
            components.swap(selectedIdx, selectedIdx - 1)
        }
    }

    fun onComponentMoveDownClick() = selectedComponent?.let {
        val selectedIdx = components.indexOf(it)
        if (selectedIdx < components.size - 1) {
            components.swap(selectedIdx, selectedIdx + 1)
        }
    }
}