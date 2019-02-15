package ru.jr2.edit.presentation.view.group

import ru.jr2.edit.data.db.repository.GroupDbRepository
import ru.jr2.edit.domain.model.Group
import ru.jr2.edit.presentation.view.BaseEditFragment
import ru.jr2.edit.presentation.viewmodel.BaseEditViewModel
import tornadofx.borderpane
import tornadofx.fieldset
import tornadofx.form

class GroupEditFragment : BaseEditFragment<Group, GroupEditViewModel>() {
    override val viewModel = GroupEditViewModel(paramItemId)

    override val root = borderpane {
        center = form {
            fieldset {

            }
        }
    }
}

class GroupEditViewModel(
    groupId: Int
) : BaseEditViewModel<Group>(groupId, GroupDbRepository(), Group()) {

}

