package ru.jr2.edit.domain.model

import javafx.beans.property.SimpleStringProperty
import ru.jr2.edit.domain.entity.GroupEntity
import ru.jr2.edit.domain.misc.GroupType
import tornadofx.getValue
import tornadofx.setValue

class Group(id: Int = 0) : BaseModel(id) {
    val pName = SimpleStringProperty()
    var name: String by pName

    val pGroupType = SimpleStringProperty()
    var groupType: String by pGroupType

    companion object {
        fun fromEntity(groupEntity: GroupEntity): Group =
            Group(groupEntity.id.value).apply {
                name = groupEntity.name
                groupType = GroupType.fromCode(groupEntity.groupType).str
            }
    }
}