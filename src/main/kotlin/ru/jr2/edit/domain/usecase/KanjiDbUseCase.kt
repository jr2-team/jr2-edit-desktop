package ru.jr2.edit.domain.usecase

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.jr2.edit.EditApp
import ru.jr2.edit.data.db.repository.KanjiDbRepository
import ru.jr2.edit.data.db.repository.KanjiReadingDbRepository
import ru.jr2.edit.data.db.table.KanjiComponentTable
import ru.jr2.edit.data.db.table.KanjiReadingTable
import ru.jr2.edit.data.db.table.KanjiTable
import ru.jr2.edit.domain.dto.KanjiDto
import ru.jr2.edit.domain.entity.KanjiEntity
import ru.jr2.edit.domain.misc.JlptLevel
import ru.jr2.edit.domain.misc.KanjiReadingType
import ru.jr2.edit.presentation.kanji.model.KanjiModel
import ru.jr2.edit.presentation.kanji.model.KanjiReadingModel

class KanjiDbUseCase(
    private val db: Database = EditApp.instance.db,
    private val kanjiRepo: KanjiDbRepository = KanjiDbRepository(db),
    private val kanjiReadingRepo: KanjiReadingDbRepository = KanjiReadingDbRepository(db)
) {
    @Suppress("SENSELESS_COMPARISON")
    fun getAllKanjiWithReadings(): List<KanjiDto> = transaction {
        val kanjiMap = hashMapOf<Int, KanjiDto>()
        KanjiTable.leftJoin(
            KanjiReadingTable,
            { KanjiTable.id },
            { KanjiReadingTable.kanji }
        ).selectAll().map {
            val kanjiId = it[KanjiTable.id].value
            if (!kanjiMap.containsKey(kanjiId)) {
                kanjiMap[kanjiId] = KanjiDto(
                    id = kanjiId,
                    kanji = it[KanjiTable.kanji],
                    interpretation = it[KanjiTable.interpretation] ?: "",
                    jlptLevel = JlptLevel.fromCode(it[KanjiTable.jlptLevel] ?: 0).str
                )
            }
            /* Проверка на null необходима не смотря на предупрждения компилятора
            , поскольку у канджи могут отсутствовать он-/кун- чтения */
            if (it[KanjiReadingTable.id] != null) {
                val reading = it[KanjiReadingTable.reading]
                when (it[KanjiReadingTable.readingType]) {
                    0 -> kanjiMap[kanjiId]?.onReadings?.apply {
                        if (isNotEmpty()) append(", $reading") else append(reading)
                    }
                    1 -> kanjiMap[kanjiId]?.kunReadings?.apply {
                        if (isNotEmpty()) append(", $reading") else append(reading)
                    }
                }
            }
        }
        kanjiMap.values.toList()
    }

    fun getKanjiComponents(kanjiId: Int): List<KanjiModel> = transaction(db) {
        val componentAlias = KanjiComponentTable.alias("kanji_component")
        KanjiTable
            .innerJoin(
                componentAlias,
                { KanjiTable.id },
                { componentAlias[KanjiComponentTable.kanjiComponentId] }
            )
            .slice(KanjiTable.columns)
            .select {
                componentAlias[KanjiComponentTable.kanji] eq KanjiEntity[kanjiId].id
            }
            .orderBy(componentAlias[KanjiComponentTable.order])
            .map { KanjiModel.fromEntity(KanjiEntity.wrapRow(it)) }
    }

    fun saveKanjiWithComponentsAndReadings(
        kanji: KanjiModel,
        readings: List<KanjiReadingModel>? = null,
        components: List<KanjiModel>? = null
    ): KanjiModel = transaction(db) {
        val newKanji = kanjiRepo.insertUpdate(kanji)
        kanjiReadingRepo.deleteByKanjiId(newKanji.id)
        readings?.run {
            this.forEach { it.kanji = newKanji.id }
            kanjiReadingRepo.insertUpdate(this)
        }
        components?.run { insertUpdateKanjiComponents(newKanji, this) }
        newKanji
    }

    fun deleteKanjiWithComponentsAndReadings(kanji: KanjiModel) = transaction(db) {
        /* Не смотря на то, что в foreign key стоит каскадное удаление,
        * оно непроисходит, поэтому приходится иметь по
        * роуту для удаления каждого состоявляющего канджи */
        // TODO: Fix cascade dropping
        kanjiReadingRepo.deleteByKanjiId(kanji.id)
        deleteKanjiComponents(kanji)
        kanjiRepo.delete(kanji)
    }

    fun deleteKanjiWithComponentsAndReadings(kanjiId: Int) = transaction(db) {
        val kanjiToDelete = kanjiRepo.getById(kanjiId)
        deleteKanjiWithComponentsAndReadings(kanjiToDelete)
    }

    fun saveParsedKanjisWithReadings(
        kanjisWithReadings: List<Pair<KanjiModel, List<KanjiReadingModel>?>>
    ) = transaction(db) {
        val insertedKanjiIds = KanjiTable.batchInsert(kanjisWithReadings.map { it.first }) {
            this[KanjiTable.kanji] = it.kanji
            this[KanjiTable.strokeCount] = it.strokeCount
            this[KanjiTable.interpretation] = it.interpretation
            this[KanjiTable.frequency] = it.frequency
            this[KanjiTable.grade] = it.grade
            this[KanjiTable.jlptLevel] = JlptLevel.fromStr(it.jlptLevel).code
        }.map { it[KanjiTable.id].value }

        insertedKanjiIds.forEachIndexed { idx, kanjiId ->
            kanjisWithReadings[idx].second?.forEach { it.kanji = kanjiId }
        }

        KanjiReadingTable.batchInsert(
            kanjisWithReadings.filter { it.second != null }.flatMap { it.second!! }
        ) {
            this[KanjiReadingTable.reading] = it.reading
            this[KanjiReadingTable.readingType] = KanjiReadingType.fromStr(it.readingType).code
            this[KanjiReadingTable.priority] = it.priority
            this[KanjiReadingTable.isAnachronism] = it.isAnachronism
            this[KanjiReadingTable.kanji] = EntityID(it.kanji, KanjiTable)
        }
    }

    private fun insertUpdateKanjiComponents(
        kanji: KanjiModel,
        components: List<KanjiModel>
    ) = transaction(db) {
        deleteKanjiComponents(kanji)
        var orderIdx = -1
        KanjiComponentTable.batchInsert(components) {
            this[KanjiComponentTable.kanji] = KanjiEntity[kanji.id].id
            this[KanjiComponentTable.kanjiComponentId] = KanjiEntity[it.id].id
            this[KanjiComponentTable.order] = ++orderIdx
        }
    }

    private fun deleteKanjiComponents(kanji: KanjiModel) = transaction(db) {
        KanjiComponentTable.deleteWhere {
            KanjiComponentTable.kanji eq kanji.id
        }
    }
}