package ru.jr2.edit.presentation.viewmodel.moji

import javafx.beans.property.SimpleStringProperty
import ru.jr2.edit.data.db.repository.MojiDbRepository
import ru.jr2.edit.domain.model.Moji
import ru.jr2.edit.openDialogFragment
import ru.jr2.edit.presentation.view.moji.MojiSearchFragment
import ru.jr2.edit.presentation.view.moji.MojiSelectedEvent
import tornadofx.ItemViewModel

class MojiEditItemViewModel(
    mojiId: Int,
    private val mojiRepository: MojiDbRepository = MojiDbRepository()
) : ItemViewModel<Moji>() {
    val value = bind(Moji::valueProp)
    val strokeCount = bind(Moji::strokeCountProp)
    val onReading = bind(Moji::onReadingProp)
    val kunReading = bind(Moji::kunReadingProp)

    val mojiComponentStringProp = SimpleStringProperty(String())
    private val mojiComponents = mutableListOf<Moji>()

    init {
        item = mojiRepository.getById(mojiId)
        subscribeOnEventBus()
    }

    fun onShowMojiSearchFragment() {
        MojiSearchFragment().openDialogFragment()
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