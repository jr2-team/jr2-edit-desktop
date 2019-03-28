package ru.jr2.edit.presentation.base.viewmodel

import ru.jr2.edit.data.db.repository.BaseDbRepository
import ru.jr2.edit.presentation.base.model.BaseModel
import ru.jr2.edit.util.EditMode
import tornadofx.FXEvent
import tornadofx.ItemViewModel

abstract class BaseEditViewModel<T : BaseModel>(
    modelId: Int,
    internal val repository: BaseDbRepository<T>,
    initBaseModel: T
) : ItemViewModel<T>(initBaseModel) {
    internal val mode: EditMode = if (modelId == 0) EditMode.CREATE else EditMode.UPDATE

    init {
        if (mode == EditMode.UPDATE) {
            item = repository.getById(modelId)
        }
    }

    open fun onSaveClick(
        // @formatter: off
        doOnSave: () -> Unit = { fire(ItemSavedEvent(true)) }
        // @formatter: on
    ) {
        commit()
        when (mode) {
            EditMode.UPDATE -> repository.insertUpdate(item)
            EditMode.CREATE -> repository.insert(item)
        }
        doOnSave.invoke()
    }

    class ItemSavedEvent(val isSaved: Boolean) : FXEvent()
}

