package ru.jr2.edit.data.db.repository

import org.jetbrains.exposed.sql.transactions.transaction
import ru.jr2.edit.data.db.table.GroupTable
import ru.jr2.edit.domain.entity.GroupEntity
import ru.jr2.edit.domain.misc.GroupType
import ru.jr2.edit.domain.model.GroupModel

class GroupDbRepository : BaseDbRepository<GroupModel>() {
    override fun getById(id: Int): GroupModel = transaction(db) {
        GroupModel.fromEntity(GroupEntity[id])
    }

    override fun getById(vararg id: Int): List<GroupModel> = transaction(db) {
        id.map { GroupModel.fromEntity(GroupEntity[it]) }
    }

    override fun getAll(): List<GroupModel> = transaction(db) {
        GroupEntity.all().map { GroupModel.fromEntity(it) }
    }

    fun getByGroupType(groupType: GroupType): List<GroupModel> = transaction(db) {
        GroupEntity.find {
            GroupTable.groupType eq groupType.code
        }.map {
            GroupModel.fromEntity(it)
        }
    }

    override fun insert(model: GroupModel): GroupModel {
        return GroupModel()
    }

    override fun insertUpdate(model: GroupModel): GroupModel {
        return GroupModel()
    }

    override fun delete(o: GroupModel) {

    }
}