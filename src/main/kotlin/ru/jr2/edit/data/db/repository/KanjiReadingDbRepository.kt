package ru.jr2.edit.data.db.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import ru.jr2.edit.EditApp
import ru.jr2.edit.data.db.table.KanjiReadingTable
import ru.jr2.edit.domain.entity.KanjiReadingEntity
import ru.jr2.edit.domain.model.KanjiReading

class KanjiReadingDbRepository(
    override val db: Database = EditApp.instance.db
) : BaseDbRepository<KanjiReading>(db) {
    override fun getById(id: Int): KanjiReading = transaction(db) {
        KanjiReading.fromEntity(KanjiReadingEntity[id])
    }

    override fun getById(vararg id: Int): List<KanjiReading> = transaction(db) {
        id.map { KanjiReading.fromEntity(KanjiReadingEntity[it]) }
    }

    override fun getAll(): List<KanjiReading> = transaction(db) {
        KanjiReadingEntity.all().map { KanjiReading.fromEntity(it) }
    }

    fun getByKanjiId(kanjiId: Int): List<KanjiReading> = transaction(db) {
        KanjiReadingEntity.find {
            KanjiReadingTable.kanji eq kanjiId
        }.map {
            KanjiReading.fromEntity(it)
        }
    }

    override fun insert(model: KanjiReading): KanjiReading = transaction(db) {
        val newKanjiReading = KanjiReadingEntity.new {
            updateWithModel(model)
        }
        KanjiReading.fromEntity(newKanjiReading)
    }

    override fun insertUpdate(model: KanjiReading): KanjiReading = transaction(db) {
        KanjiReadingEntity.findById(model.id)?.run {
            updateWithModel(model)
            getById(model.id)
        } ?: insert(model)
    }

    fun insertUpdate(models: List<KanjiReading>): List<KanjiReading> = transaction(db) {
        models.map { insertUpdate(it) }
    }

    override fun delete(model: KanjiReading) = transaction(db) {
        KanjiReadingEntity[model.id].delete()
    }

    fun deleteByKanjiId(kanjiId: Int) = transaction(db) {
        KanjiReadingEntity.find {
            KanjiReadingTable.kanji eq kanjiId
        }.forEach { it.delete() }
    }
}