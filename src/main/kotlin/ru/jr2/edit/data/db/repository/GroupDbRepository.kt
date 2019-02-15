package ru.jr2.edit.data.db.repository

import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import ru.jr2.edit.data.db.table.GroupTable
import ru.jr2.edit.data.db.table.MojiTable
import ru.jr2.edit.domain.entity.GroupEntity
import ru.jr2.edit.domain.misc.GroupType
import ru.jr2.edit.domain.model.Group

class GroupDbRepository : BaseDbRepository<Group>() {
    override fun getById(id: Int): Group = transaction(db) {
        Group.fromEntity(GroupEntity[id])
    }

    override fun getById(vararg id: Int): List<Group> = transaction(db) {
        id.map { Group.fromEntity(GroupEntity[it]) }
    }

    override fun getAll(): List<Group> = transaction(db) {
        GroupEntity.all().map { Group.fromEntity(it) }
    }

    fun getByGroupType(groupType: GroupType): List<Group> = transaction(db) {
        GroupEntity.find {
            GroupTable.groupType eq groupType.code
        }.map {
            Group.fromEntity(it)
        }
    }

    override fun insert(o: Group): Group {
        return Group()
    }

    override fun insertUpdate(o: Group): Group {
        return Group()
    }

    override fun delete(o: Group) {

    }

}