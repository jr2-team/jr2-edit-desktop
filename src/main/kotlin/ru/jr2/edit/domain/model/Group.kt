package ru.jr2.edit.domain.model

import javafx.beans.property.SimpleStringProperty
import ru.jr2.edit.domain.entity.GroupEntity
import ru.jr2.edit.domain.misc.GroupType
import tornadofx.getValue
import tornadofx.setValue

class Group(
    id: Int = 0,
    value: String = String(),
    groupType: String = GroupType.WORD_GROUP.str
) : BaseModel(id, value) {
    val pGroupType = SimpleStringProperty(groupType)
    var groupType: String by pGroupType

    companion object {
        fun fromEntity(groupEntity: GroupEntity): Group = with(groupEntity) {
            Group(id.value, value, GroupType.fromCode(groupType).str)
        }
    }
}