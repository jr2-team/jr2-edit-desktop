package ru.jr2.edit.domain.entity

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import ru.jr2.edit.data.db.table.GroupTable

class GroupEntity(id: EntityID<Int>) : IntEntity(id) {
    var value by GroupTable.value
    var isUserGroup by GroupTable.isUserGroup
    var groupType by GroupTable.groupType
//    var creationDate by GroupTable.creationDate
//    var lastStudyDate by GroupTable.lastStudyDate
//    var section by GroupTable.section

    companion object : IntEntityClass<GroupEntity>(GroupTable)
}