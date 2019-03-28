package ru.jr2.edit.presentation.group.viewmodel

import ru.jr2.edit.data.db.repository.GroupDbRepository
import ru.jr2.edit.presentation.group.model.GroupModel
import ru.jr2.edit.presentation.base.viewmodel.BaseEditViewModel

class GroupEditViewModel(
    groupId: Int
) : BaseEditViewModel<GroupModel>(groupId, GroupDbRepository(),
    GroupModel()
)