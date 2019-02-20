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
import ru.jr2.edit.domain.model.Kanji
import ru.jr2.edit.domain.model.KanjiReading

class KanjiUseCase(
    private val db: Database = EditApp.instance.db,
    private val kanjiDbRepository: KanjiDbRepository = KanjiDbRepository(db),
    private val kanjiReadingDbRepository: KanjiReadingDbRepository = KanjiReadingDbRepository(db)
) {
    fun getAllKanjiWithReadings(): List<KanjiDto> = transaction(db) {
        val kanjiMap = mutableMapOf<Int, KanjiDto>()
        KanjiTable.leftJoin(
            KanjiReadingTable,
            { KanjiTable.id },
            { KanjiReadingTable.kanji }
        ).selectAll().map {
            val kanjiId = it[KanjiTable.id].value
            if (!kanjiMap.containsKey(kanjiId)) {
                kanjiMap[kanjiId] = KanjiDto(
                    kanjiId,
                    it[KanjiTable.kanji],
                    it[KanjiTable.interpretation] ?: "",
                    jlptLevel = JlptLevel.fromCode(it[KanjiTable.jlptLevel] ?: 0).str
                )
            }
            /* Проверка на null нужна, поскольку у канджи могут отсутствовать
            * он-/кун- чтения */
            if (it[KanjiReadingTable.id] is EntityID<Int>) {
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
        return@transaction kanjiMap.values.toList()
    }

    fun getKanjiComponents(kanjiId: Int): List<Kanji> = transaction(db) {
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
            .map {
                Kanji.fromEntity(KanjiEntity.wrapRow(it))
            }
    }

    fun saveKanjiWithComponentsAndReadings(
        kanji: Kanji,
        readings: List<KanjiReading>? = null,
        components: List<Kanji>? = null
    ): Kanji = transaction(db) {
        val newKanji = kanjiDbRepository.insertUpdate(kanji)
        readings?.run {
            this.forEach { it.kanji = newKanji.id }
            kanjiReadingDbRepository.insertUpdate(this)
        }
        components?.run { insertUpdateComponents(newKanji, this) }
        newKanji
    }

    fun deleteKanjiWithComponentsAndReadings(kanji: Kanji) = transaction(db) {
        /* Не смотря на то, что в форин кеях стоит каскадное удаление,
        * exposed не хочет его производить, поэтому приходится иметь по
        * роуту для удаления каждого состоявляющего канджи */
        kanjiReadingDbRepository.deleteByKanjiId(kanji.id)
        deleteKanjiComponents(kanji)
        kanjiDbRepository.delete(kanji)
    }

    fun deleteKanjiWithComponentsAndReadings(kanjiId: Int) = transaction(db) {
        val kanjiToDelete = kanjiDbRepository.getById(kanjiId)
        deleteKanjiWithComponentsAndReadings(kanjiToDelete)
    }

    fun fastKanjiWithReadingsInsertion(
        kanjisWithReadings: List<Pair<Kanji, List<KanjiReading>?>>
    ) = transaction(db) {
        val insertedKanjiId = KanjiTable.batchInsert(kanjisWithReadings.map { it.first }) {
            this[KanjiTable.kanji] = it.kanji
            this[KanjiTable.strokeCount] = it.strokeCount
            this[KanjiTable.interpretation] = it.interpretation
            this[KanjiTable.frequency] = it.frequency
            this[KanjiTable.grade] = it.grade
            this[KanjiTable.jlptLevel] = JlptLevel.fromStr(it.jlptLevel).code
        }.map {
            KanjiEntity.wrapRow(it).id.value
        }

        insertedKanjiId.forEachIndexed { idx, kanjiId ->
            kanjisWithReadings[idx].second?.forEach {
                it.kanji = kanjiId
            }
        }

        KanjiReadingTable.batchInsert(
            kanjisWithReadings
                .filter { it.second is List<KanjiReading> }
                .flatMap { it.second!! }
        ) {
            this[KanjiReadingTable.reading] = it.reading
            this[KanjiReadingTable.readingType] = it.readingType
            this[KanjiReadingTable.priority] = it.priority
            this[KanjiReadingTable.isAnachronism] = it.isAnachronism
            this[KanjiReadingTable.kanji] = EntityID(it.kanji, KanjiTable)
        }
    }

    private fun insertUpdateComponents(kanji: Kanji, components: List<Kanji>) = transaction(db) {
        deleteKanjiComponents(kanji)
        var orderIdx = -1
        KanjiComponentTable.batchInsert(components) {
            this[KanjiComponentTable.kanji] = KanjiEntity[kanji.id].id
            this[KanjiComponentTable.kanjiComponentId] = KanjiEntity[it.id].id
            this[KanjiComponentTable.order] = ++orderIdx
        }
    }

    private fun deleteKanjiComponents(kanji: Kanji) = transaction(db) {
        KanjiComponentTable.deleteWhere {
            KanjiComponentTable.kanji eq kanji.id
        }
    }
}