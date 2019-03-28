package ru.jr2.edit.presentation.group.model

import javafx.beans.property.SimpleStringProperty
import ru.jr2.edit.domain.entity.GroupEntity
import ru.jr2.edit.util.GroupType
import ru.jr2.edit.presentation.base.model.BaseModel
import tornadofx.getValue
import tornadofx.setValue

class GroupModel(id: Int = 0) : BaseModel(id) {
    val pName = SimpleStringProperty()
    var name: String by pName

    val pGroupType = SimpleStringProperty()
    var groupType: String by pGroupType

    companion object {
        fun fromEntity(groupEntity: GroupEntity): GroupModel =
            GroupModel(groupEntity.id.value).apply {
                name = groupEntity.name
                groupType = GroupType.fromCode(groupEntity.groupType).str
            }
    }
}