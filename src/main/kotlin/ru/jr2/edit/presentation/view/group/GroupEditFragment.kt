package ru.jr2.edit.presentation.view.group

import ru.jr2.edit.presentation.model.GroupModel
import ru.jr2.edit.presentation.view.BaseEditFragment
import ru.jr2.edit.presentation.viewmodel.group.GroupEditViewModel
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

