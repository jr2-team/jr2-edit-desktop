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
import ru.jr2.edit.domain.model.Word

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

    override fun insert(moji: Moji): Moji = transaction(db) {
        val newMoji = MojiEntity.new {
            value = moji.value
            strokeCount = moji.strokeCount
            kunReading = moji.kunReading
            onReading = moji.onReading
            interpretation = moji.interpretation
            jlptLevel = JlptLevel.fromStr(moji.jlptLevel).code
            mojiType = MojiType.fromStr(moji.mojiType).code
        }
        Moji.fromEntity(newMoji)
    }

    fun insertAll(mojis: List<Moji>) {
        transaction(db) {
            MojiTable.batchInsert(mojis) {
                this[MojiTable.value] = it.value
                this[MojiTable.strokeCount] = it.strokeCount
                this[MojiTable.onReading] = it.onReading
                this[MojiTable.kunReading] = it.kunReading
                this[MojiTable.interpretation] = it.interpretation
                this[MojiTable.jlptLevel] = JlptLevel.fromStr(it.jlptLevel).code
                this[MojiTable.mojiType] = MojiType.fromStr(it.mojiType).code
            }
        }
    }

    override fun insertUpdate(moji: Moji): Moji = transaction(db) {
        MojiEntity.findById(moji.id)?.run {
            value = moji.value
            strokeCount = moji.strokeCount
            kunReading = moji.kunReading
            onReading = moji.onReading
            interpretation = moji.interpretation
            jlptLevel = JlptLevel.fromStr(moji.jlptLevel).code
            mojiType = MojiType.fromStr(moji.mojiType).code
            getById(moji.id)
        } ?: insert(moji)
    }


    override fun delete(moji: Moji) = transaction(db) {
        ComponentKanjiTable.deleteWhere {
            ComponentKanjiTable.moji eq MojiEntity[moji.id].id
        }
        MojiEntity[moji.id].delete()
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

    fun doesExist(moji: Moji): Boolean = transaction(db) {
        val mojisFound = MojiEntity.find {
            (MojiTable.value eq moji.value).and(
                MojiTable.mojiType eq MojiType.fromStr(moji.mojiType).code
            )
        }.count()
        mojisFound > 0
    }

    fun getByWord(word: Word): List<Moji> = transaction(db) {
        word.value.asSequence().map { it }.distinct().map { m ->
            MojiEntity.find {
                (MojiTable.value eq m.toString())
                    .and(MojiTable.mojiType eq MojiType.KANJI.code)
            }.firstOrNull()
        }.map {
            it?.run { Moji.fromEntity(it) }
        }
        listOf<Moji>()
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