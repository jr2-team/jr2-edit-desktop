package ru.jr2.edit.presentation.group.view

import ru.jr2.edit.presentation.base.view.BaseEditFragment
import ru.jr2.edit.presentation.group.model.GroupModel
import ru.jr2.edit.presentation.group.viewmodel.GroupEditViewModel
import tornadofx.borderpane
import tornadofx.fieldset
import tornadofx.form

class GroupEditFragment : BaseEditFragment<GroupModel, GroupEditViewModel>() {
    override val viewModel = GroupEditViewModel(paramItemId)

    override val root = borderpane {
        center = form {
            fieldset {

            }
        }
    }
}

