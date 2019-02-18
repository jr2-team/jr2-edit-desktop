package ru.jr2.edit.data.db.repository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.jr2.edit.EditApp
import ru.jr2.edit.data.db.table.ComponentKanjiTable
import ru.jr2.edit.data.db.table.MojiTable
import ru.jr2.edit.domain.entity.MojiEntity
import ru.jr2.edit.domain.misc.JlptLevel
import ru.jr2.edit.domain.misc.MojiType
import ru.jr2.edit.domain.model.Moji

class MojiDbRepository(
    override val db: Database = EditApp.instance.db
) : BaseDbRepository<Moji>(db) {
    override fun getById(id: Int): Moji = transaction(db) {
        Moji.fromEntity(MojiEntity[id])
    }

    override fun getById(vararg id: Int): List<Moji> = transaction(db) {
        id.map { Moji.fromEntity(MojiEntity[it]) }
    }

    override fun getAll(): List<Moji> = transaction(db) {
        MojiEntity.all().map { Moji.fromEntity(it) }
    }

    override fun insert(model: Moji): Moji = transaction(db) {
        val newMoji = MojiEntity.new {
            moji = model.moji
            strokeCount = model.strokeCount
            interpretation = model.interpretation
            frequency = model.frequency
            grade = model.grade
            svg = model.svg
            jlptLevel = JlptLevel.fromStr(model.jlptLevel).code
            mojiType = MojiType.fromStr(model.mojiType).code
        }
        Moji.fromEntity(newMoji)
    }

    fun insertAll(mojis: List<Moji>) {
        transaction(db) {
            MojiTable.batchInsert(mojis) {
                this[MojiTable.moji] = it.moji
                this[MojiTable.strokeCount] = it.strokeCount
                this[MojiTable.interpretation] = it.interpretation
                this[MojiTable.frequency] = it.frequency
                this[MojiTable.grade] = it.grade
                this[MojiTable.svg] = it.svg
                this[MojiTable.jlptLevel] = JlptLevel.fromStr(it.jlptLevel).code
                this[MojiTable.mojiType] = MojiType.fromStr(it.mojiType).code
            }
        }
    }

    override fun insertUpdate(model: Moji): Moji = transaction(db) {
        MojiEntity.findById(model.id)?.run {
            moji = model.moji
            strokeCount = model.strokeCount
            interpretation = model.interpretation
            frequency = model.frequency
            grade = model.grade
            svg = model.svg
            jlptLevel = JlptLevel.fromStr(model.jlptLevel).code
            mojiType = MojiType.fromStr(model.mojiType).code
            getById(model.id)
        } ?: insert(model)
    }


    override fun delete(model: Moji) = transaction(db) {
        ComponentKanjiTable.deleteWhere {
            ComponentKanjiTable.moji eq MojiEntity[model.id].id
        }
        MojiEntity[model.id].delete()
    }

    fun getComponentsOfMoji(mojiId: Int): List<Moji> = transaction(db) {
        val componentAlias = ComponentKanjiTable.alias("component_moji")
        MojiTable
            .innerJoin(
                componentAlias,
                { MojiTable.id },
                { componentAlias[ComponentKanjiTable.mojiComponentId] }
            )
            .slice(MojiTable.columns)
            .select {
                componentAlias[ComponentKanjiTable.moji] eq MojiEntity[mojiId].id
            }
            .orderBy(componentAlias[ComponentKanjiTable.order])
            .map {
                Moji.fromEntity(MojiEntity.wrapRow(it))
            }
    }

    // TODO: Придумать мхеанизм поиска, перевод каны в романджи?
    fun getBySearchQuery(query: String): List<Moji> = transaction(db) {
        MojiEntity.find {
            MojiTable.interpretation.upperCase() like "%$query%".toUpperCase()
        }.map {
            Moji.fromEntity(it)
        }
    }

    @Suppress("NAME_SHADOWING")
    fun insertUpdateMojiComponent(moji: Moji, components: List<Moji>) = transaction(db) {
        val moji = insertUpdate(moji)
        /*
        * Поскольку в Exposed нет (или пока нет) массового обновления записей,
        * то перед созданием приходится полностью удалять компоненты
        */
        ComponentKanjiTable.deleteWhere {
            ComponentKanjiTable.moji eq MojiEntity[moji.id].id
        }
        var orderIdx = -1
        ComponentKanjiTable.batchInsert(components) {
            this[ComponentKanjiTable.moji] = MojiEntity[moji.id].id
            this[ComponentKanjiTable.mojiComponentId] = MojiEntity[it.id].id
            this[ComponentKanjiTable.order] = ++orderIdx
        }
    }
}