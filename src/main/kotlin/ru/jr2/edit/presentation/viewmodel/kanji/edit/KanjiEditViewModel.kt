package ru.jr2.edit.presentation.viewmodel.kanji.edit

import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.stage.StageStyle
import ru.jr2.edit.data.db.repository.KanjiDbRepository
import ru.jr2.edit.data.db.repository.KanjiReadingDbRepository
import ru.jr2.edit.domain.model.KanjiModel
import ru.jr2.edit.domain.model.KanjiReadingModel
import ru.jr2.edit.domain.usecase.KanjiDbUseCase
import ru.jr2.edit.presentation.view.kanji.edit.KanjiEditComponentFragment
import ru.jr2.edit.presentation.view.kanji.edit.KanjiEditReadingFragment
import ru.jr2.edit.presentation.view.kanji.edit.KanjiEditSearchFragment
import ru.jr2.edit.presentation.viewmodel.BaseEditViewModel
import ru.jr2.edit.presentation.viewmodel.EditMode
import tornadofx.Scope
import tornadofx.find
import tornadofx.onChange
import tornadofx.swap

class KanjiEditViewModel(
    mojiId: Int,
    private val kanjiDbUseCase: KanjiDbUseCase = KanjiDbUseCase()
) : BaseEditViewModel<KanjiModel>(mojiId, KanjiDbRepository(), KanjiModel()) {
    val pKanji = bind(KanjiModel::pKanji)
    val pStrokeCount = bind(KanjiModel::pStrokeCount)
    val pInterpretation = bind(KanjiModel::pInterpretation)
    val pFrequency = bind(KanjiModel::pFrequency)
    val pGrade = bind(KanjiModel::pGrade)
    val pJlptLevel = bind(KanjiModel::pJlptLevel)

    val pComponents = SimpleStringProperty(String())

    val readings: ObservableList<KanjiReadingModel> = FXCollections.observableArrayList<KanjiReadingModel>()
    val components: ObservableList<KanjiModel> = FXCollections.observableArrayList<KanjiModel>()

    init {
        components.onChange {
            pComponents.value = components.joinToString { c -> c.kanji }
        }
        if (mode == EditMode.UPDATE) {
            components.addAll(kanjiDbUseCase.getKanjiComponents(mojiId))
            readings.addAll(KanjiReadingDbRepository().getByKanjiId(mojiId))
        }
    }

    // Чтения канджи
    fun onKanjiReadingAddClick() {
        find<KanjiEditReadingFragment>(Scope(this))
            .openModal(StageStyle.UTILITY, resizable = false)
    }

    fun onKanjiReadingEditClick() {

    }

    fun onKanjiReadingDeleteClick() {

    }

    // Компоненты канджи
    var selectedComponent: KanjiModel? = null

    fun onKanjiSearchClick() {
        find<KanjiEditSearchFragment>(Scope(this))
            .openModal(StageStyle.UTILITY, resizable = false)
    }

    fun onEditComponentClick() {
        find<KanjiEditComponentFragment>(Scope(this))
            .openModal(StageStyle.UTILITY, resizable = false)
    }

    fun onComponentAddClick() = selectedComponent?.let {
        if (!components.contains(it)) components.add(it)
    }

    fun onComponentRemoveClick() = selectedComponent?.let {
        components.remove(it)
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

    override fun onSaveClick(doOnSave: () -> Unit) {
        commit()
        kanjiDbUseCase.saveKanjiWithComponentsAndReadings(item, readings, components)
        fire(ItemSavedEvent(true))
    }
}