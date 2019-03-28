package ru.jr2.edit.presentation.kanji.viewmodel.edit

import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.stage.StageStyle
import ru.jr2.edit.data.db.repository.KanjiDbRepository
import ru.jr2.edit.data.db.repository.KanjiReadingDbRepository
import ru.jr2.edit.domain.usecase.KanjiDbUseCase
import ru.jr2.edit.presentation.base.viewmodel.BaseEditViewModel
import ru.jr2.edit.presentation.kanji.model.KanjiModel
import ru.jr2.edit.presentation.kanji.model.KanjiReadingModel
import ru.jr2.edit.presentation.kanji.view.edit.KanjiEditComponentFragment
import ru.jr2.edit.presentation.kanji.view.edit.KanjiEditSearchFragment
import ru.jr2.edit.presentation.kanji.view.edit.KanjiReadingEditFragment
import ru.jr2.edit.util.EditMode
import tornadofx.Scope
import tornadofx.find
import tornadofx.onChange
import tornadofx.swap

class KanjiEditViewModel(
    mojiId: Int,
    private val kanjiDbUseCase: KanjiDbUseCase = KanjiDbUseCase()
) : BaseEditViewModel<KanjiModel>(
    mojiId, KanjiDbRepository(),
    KanjiModel()
) {
    val pKanji = bind(KanjiModel::pKanji)
    val pStrokeCount = bind(KanjiModel::pStrokeCount)
    val pInterp = bind(KanjiModel::pInterp)
    val pFrequency = bind(KanjiModel::pFrequency)
    val pGrade = bind(KanjiModel::pGrade)
    val pJlptLevel = bind(KanjiModel::pJlptLevel)

    val pComponents = SimpleStringProperty(String())

    val kanjiReadings: ObservableList<KanjiReadingModel> = FXCollections.observableArrayList()
    val kanjiComponents: ObservableList<KanjiModel> = FXCollections.observableArrayList()

    init {
        kanjiComponents.onChange {
            pComponents.value = kanjiComponents.joinToString { c -> c.kanji }
        }
        if (mode == EditMode.UPDATE) {
            kanjiComponents.addAll(kanjiDbUseCase.getKanjiComponents(mojiId))
            kanjiReadings.addAll(KanjiReadingDbRepository().getByKanjiId(mojiId))
        }
    }

    // Чтения кандзии
    var selectedKanjiReading: KanjiReadingModel? = null

    fun onKanjiReadingAddClick() {
        find<KanjiReadingEditFragment>(Scope(this))
            .openModal(StageStyle.UTILITY, resizable = false)
    }

    fun onKanjiReadingEditClick() {
        find<KanjiReadingEditFragment>(
            Scope(this),
            Pair(KanjiReadingEditFragment::paramKanjiReading, selectedKanjiReading)
        ).openModal(StageStyle.UTILITY, resizable = false)
    }

    fun onKanjiReadingDeleteClick() = selectedKanjiReading?.let {
        kanjiReadings.remove(selectedKanjiReading)
    }

    fun onKanjiReadingSaveClick(kanjiReading: KanjiReadingModel) {
        val idx = kanjiReadings.indexOf(kanjiReading)
        if (idx > -1) {
            kanjiReadings.add(idx, kanjiReading)
            kanjiReadings.removeAt(idx + 1)
        } else {
            kanjiReadings.add(kanjiReading)
        }
    }

    // Компоненты кандзи
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
        if (!kanjiComponents.contains(it)) kanjiComponents.add(it)
    }

    fun onComponentRemoveClick() = selectedComponent?.let {
        kanjiComponents.remove(it)
    }

    fun onComponentMoveUpClick() = selectedComponent?.let {
        val selectedIdx = kanjiComponents.indexOf(it)
        if (selectedIdx > 0) {
            kanjiComponents.swap(selectedIdx, selectedIdx - 1)
        }
    }

    fun onComponentMoveDownClick() = selectedComponent?.let {
        val selectedIdx = kanjiComponents.indexOf(it)
        if (selectedIdx < kanjiComponents.size - 1) {
            kanjiComponents.swap(selectedIdx, selectedIdx + 1)
        }
    }

    override fun onSaveClick(doOnSave: () -> Unit) {
        commit()
        kanjiDbUseCase.saveKanjiWithComponentsAndReadings(
            kanji = item,
            readings = kanjiReadings,
            components = kanjiComponents
        )
        fire(ItemSavedEvent(true))
    }
}