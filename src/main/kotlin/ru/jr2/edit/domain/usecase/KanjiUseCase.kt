package ru.jr2.edit.domain.usecase

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import ru.jr2.edit.EditApp
import ru.jr2.edit.data.db.repository.KanjiDbRepository
import ru.jr2.edit.data.db.repository.KanjiReadingDbRepository
import ru.jr2.edit.data.db.table.KanjiComponentTable
import ru.jr2.edit.domain.entity.KanjiEntity
import ru.jr2.edit.domain.model.Kanji
import ru.jr2.edit.domain.model.KanjiReading

class KanjiUseCase(
    private val db: Database = EditApp.instance.db,
    private val kanjiDbRepository: KanjiDbRepository = KanjiDbRepository(db),
    private val kanjiReadingDbRepository: KanjiReadingDbRepository = KanjiReadingDbRepository(db)
) {
    fun getAllKanjiWithReadings(): List<Kanji> = transaction(db) {
        val mojis = kanjiDbRepository.getAll()
        mojis.forEach {
            it.apply {
                val kanjiReadingEntities = kanjiReadingDbRepository.getByKanjiId(id)
                onReading = kanjiReadingEntities
                    .filter { it.readingType == 0 }
                    .joinToString { it.reading }
                kunReading = kanjiReadingEntities
                    .filter { it.readingType == 1 }
                    .joinToString { it.reading }
            }
        }
        mojis
    }

    fun saveKanjiWithComponentsAndReadings(
        kanji: Kanji,
        readings: List<KanjiReading>? = null,
        components: List<Kanji>? = null
    ): Kanji = transaction(db) {
        val savedKanji = kanjiDbRepository.insertUpdate(kanji)
        if (readings is List<KanjiReading>) {
            readings.forEach { it.kanji = savedKanji.id }
            kanjiReadingDbRepository.insertUpdate(readings)
        }
        if (components is List<Kanji>) {
            insertUpdateComponents(savedKanji, components)
        }
        savedKanji
    }

    fun deleteKanjiWithComponentsAndReadings(kanji: Kanji) = transaction(db) {
        /* Не смотря на то, что в форин кеях стоит каскадное удаление,
        * exposed не хочет его производить, поэтому приходится иметь по
        * роуту для удаления каждого состоявляющего канджи */
        kanjiReadingDbRepository.deleteByKanjiId(kanji.id)
        kanjiDbRepository.deleteKanjiComponents(kanji)
        kanjiDbRepository.delete(kanji)
    }

    private fun insertUpdateComponents(kanji: Kanji, components: List<Kanji>) = transaction(db) {
        KanjiComponentTable.deleteWhere {
            KanjiComponentTable.kanji eq kanji.id
        }
        var orderIdx = -1
        KanjiComponentTable.batchInsert(components) {
            this[KanjiComponentTable.kanji] = KanjiEntity[kanji.id].id
            this[KanjiComponentTable.kanjiComponentId] = KanjiEntity[it.id].id
            this[KanjiComponentTable.order] = ++orderIdx
        }
    }
}