package ru.jr2.edit.presentation.viewmodel.moji

import javafx.beans.property.SimpleStringProperty
import ru.jr2.edit.data.db.repository.MojiDbRepository
import ru.jr2.edit.domain.model.Moji
import ru.jr2.edit.presentation.view.moji.MojiEditComponentFragment
import ru.jr2.edit.presentation.view.moji.MojiEditSearchFragment
import ru.jr2.edit.presentation.viewmodel.EditMode
import ru.jr2.edit.util.openDialogFragment
import tornadofx.ItemViewModel

class MojiEditViewModel(
    mojiId: Int,
    private val mode: EditMode = if (mojiId == 0) EditMode.CREATE else EditMode.UPDATE,
    private val mojiRepository: MojiDbRepository = MojiDbRepository()
) : ItemViewModel<Moji>() {
    val value = bind(Moji::valueProp)
    val strokeCount = bind(Moji::strokeCountProp)
    val onReading = bind(Moji::onReadingProp)
    val kunReading = bind(Moji::kunReadingProp)
    val basicInterpretation = bind(Moji::basicInterpretationProp)
    val jlptLevel = bind(Moji::jlptLevelProp)
    val mojiType = bind(Moji::mojiTypeProp)

    val mojiComponentStringProp = SimpleStringProperty(String())
    private val mojiComponents = mutableListOf<Moji>()

    init {
        item = when (mode) {
            EditMode.UPDATE -> {
                mojiComponents.addAll(mojiRepository.getComponentsOfMoji(mojiId))
                mojiRepository.getById(mojiId)
            }
            EditMode.CREATE -> Moji()
        }
        subscribeOnEventBus()
    }

    fun onShowMojiSearchFragment() {
        MojiEditSearchFragment().openDialogFragment()
    }

    fun onShowMojiEditComponentFragment() {
        MojiEditComponentFragment().openDialogFragment(
            mapOf(Pair(MojiEditComponentFragment::mojiIds, mojiComponents.map { it.id }))
        )
    }

    fun onSaveClick() {
        if (this.isValid) {
            mojiRepository.insertUpdateMojiComponent(item, mojiComponents)
            fire(MojiSavedEvent(true))
        }
    }

    private fun subscribeOnEventBus() {
        subscribe<MojiSelectedEvent> { ctx ->
            mojiComponents.find {
                it.value == ctx.moji.value
            }.run {
                if (this == null) {
                    mojiComponents.add(ctx.moji)
                    mojiComponentStringProp.value += ctx.moji.value + ' '
                }
            }
        }
    }
}