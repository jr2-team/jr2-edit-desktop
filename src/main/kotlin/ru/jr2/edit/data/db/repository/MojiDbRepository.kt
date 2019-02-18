package ru.jr2.edit.data.db.repository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.jr2.edit.EditApp
import ru.jr2.edit.data.db.table.ComponentKanjiTable
import ru.jr2.edit.data.db.table.KanjiReadingTable
import ru.jr2.edit.data.db.table.MojiTable
import ru.jr2.edit.domain.entity.MojiEntity
import ru.jr2.edit.domain.entity.updateWithModel
import ru.jr2.edit.domain.misc.JlptLevel
import ru.jr2.edit.domain.misc.MojiType
import ru.jr2.edit.domain.model.KanjiReading
import ru.jr2.edit.domain.model.Moji
import java.sql.SQLDataException

class MojiDbRepository(
    override val db: Database = EditApp.instance.db,
    private val kanjiReadingRepository: KanjiReadingDbRepository = KanjiReadingDbRepository(db)
) : BaseDbRepository<Moji>(db) {
    // TODO: Раобраться с получением чтений
    override fun getById(id: Int): Moji = transaction(db) {
        Moji.fromEntity(MojiEntity[id], kanjiReadingRepository.getByKanjiId(id))
    }

    override fun getById(vararg id: Int): List<Moji> = transaction(db) {
        id.map { Moji.fromEntity(MojiEntity[it]) }
    }

    override fun getAll(): List<Moji> = transaction(db) {
        MojiEntity.all().map { Moji.fromEntity(it, kanjiReadingRepository.getByKanjiId(it.id.value)) }
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

    override fun insert(model: Moji): Moji = transaction(db) {
        val newMoji = MojiEntity.new {
            updateWithModel(model)
        }
        Moji.fromEntity(newMoji)
    }

    override fun insertUpdate(model: Moji): Moji = transaction(db) {
        MojiEntity.findById(model.id)?.run {
            this.updateWithModel(model)
            getById(model.id)
        } ?: insert(model)
    }

    fun insertUpdate(
        model: Moji,
        readings: List<KanjiReading>? = null,
        components: List<Moji>? = null
    ): Moji = transaction(db) {
        val moji = insertUpdate(model)
        if (readings is List<KanjiReading>) insertUpdateReadings(moji, readings)
        if (components is List<Moji>) insertUpdateComponents(moji, components)
        moji
    }

    fun insertAll(models: List<Moji>): List<Moji> = transaction(db) {
        MojiTable.batchInsert(models) {
            this[MojiTable.moji] = it.moji
            this[MojiTable.strokeCount] = it.strokeCount
            this[MojiTable.interpretation] = it.interpretation
            this[MojiTable.frequency] = it.frequency
            this[MojiTable.grade] = it.grade
            this[MojiTable.svg] = it.svg
            this[MojiTable.jlptLevel] = JlptLevel.fromStr(it.jlptLevel).code
            this[MojiTable.mojiType] = MojiType.fromStr(it.mojiType).code
        }.map {
            Moji.fromEntity(MojiEntity.wrapRow(it))
        }
    }

    private fun insertUpdateReadings(moji: Moji, readings: List<KanjiReading>) = transaction(db) {
        isMojiLegalToInsert(moji)
        KanjiReadingTable.deleteWhere {
            KanjiReadingTable.kanji eq moji.id
        }
        KanjiReadingTable.batchInsert(readings) {
            this[KanjiReadingTable.reading] = it.reading
            this[KanjiReadingTable.readingType] = it.readingType
            this[KanjiReadingTable.priority] = it.priority
            this[KanjiReadingTable.isAnachronism] = it.isAnachronism
            this[KanjiReadingTable.kanji] = MojiEntity[moji.id].id
        }
    }

    private fun insertUpdateComponents(moji: Moji, components: List<Moji>) = transaction(db) {
        isMojiLegalToInsert(moji)
        ComponentKanjiTable.deleteWhere {
            ComponentKanjiTable.moji eq moji.id
        }
        var orderIdx = -1
        ComponentKanjiTable.batchInsert(components) {
            this[ComponentKanjiTable.mojiComponentId] = MojiEntity[it.id].id
            this[ComponentKanjiTable.order] = ++orderIdx
            this[ComponentKanjiTable.moji] = MojiEntity[moji.id].id
        }
    }

    override fun delete(model: Moji) = transaction(db) {
        KanjiReadingTable.deleteWhere {
            KanjiReadingTable.kanji eq MojiEntity[model.id].id
        }
        ComponentKanjiTable.deleteWhere {
            ComponentKanjiTable.moji eq MojiEntity[model.id].id
        }
        MojiEntity[model.id].delete()
    }

    private fun isMojiLegalToInsert(moji: Moji) {
        if (moji.id == 0) throw SQLDataException()
    }
}