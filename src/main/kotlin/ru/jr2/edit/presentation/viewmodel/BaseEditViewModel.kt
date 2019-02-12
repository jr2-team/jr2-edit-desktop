package ru.jr2.edit.presentation.viewmodel

import ru.jr2.edit.data.db.repository.BaseDbRepository
import ru.jr2.edit.domain.model.BaseModel
import tornadofx.ItemViewModel

abstract class BaseEditViewModel<T : BaseModel>(
    baseModelId: Int,
    repository: BaseDbRepository<T>,
    intiBaseModel: T,
    internal val mode: EditMode = if (baseModelId == 0) EditMode.CREATE else EditMode.UPDATE
) : ItemViewModel<T>(intiBaseModel) {
    init {
        if (mode == EditMode.UPDATE) {
            item = repository.getById(baseModelId)
        }
    }
}
