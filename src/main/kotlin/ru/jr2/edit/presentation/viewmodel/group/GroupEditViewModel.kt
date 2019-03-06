package ru.jr2.edit.presentation.viewmodel.group

import ru.jr2.edit.data.db.repository.GroupDbRepository
import ru.jr2.edit.presentation.model.GroupModel
import ru.jr2.edit.presentation.viewmodel.BaseEditViewModel

class GroupEditViewModel(
    groupId: Int
) : BaseEditViewModel<GroupModel>(groupId, GroupDbRepository(), GroupModel()) {

}