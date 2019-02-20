package ru.jr2.edit.presentation.viewmodel.kanji

import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.stage.StageStyle
import ru.jr2.edit.data.db.repository.KanjiDbRepository
import ru.jr2.edit.data.db.repository.KanjiReadingDbRepository
import ru.jr2.edit.domain.model.Kanji
import ru.jr2.edit.domain.model.KanjiReading
import ru.jr2.edit.domain.usecase.KanjiUseCase
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
    private val kanjiUseCase: KanjiUseCase = KanjiUseCase()
) : BaseEditViewModel<Kanji>(mojiId, KanjiDbRepository(), Kanji()) {
    val pKanji = bind(Kanji::pKanji)
    val pStrokeCount = bind(Kanji::pStrokeCount)
    val pInterpretation = bind(Kanji::pInterpretation)
    val pFrequency = bind(Kanji::pFrequency)
    val pGrade = bind(Kanji::pGrade)
    val pJlptLevel = bind(Kanji::pJlptLevel)

    val pComponents = SimpleStringProperty(String())

    val readings: ObservableList<KanjiReading> = FXCollections.observableArrayList<KanjiReading>()
    val components: ObservableList<Kanji> = FXCollections.observableArrayList<Kanji>()

    init {
        components.onChange {
            pComponents.value = components.joinToString { c -> c.kanji }
        }
        if (mode == EditMode.UPDATE) {
            components.addAll(kanjiUseCase.getKanjiComponents(mojiId))
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
    var selectedComponent: Kanji? = null

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
        kanjiUseCase.saveKanjiWithComponentsAndReadings(item, readings, components)
        fire(ItemSavedEvent(true))
    }
}